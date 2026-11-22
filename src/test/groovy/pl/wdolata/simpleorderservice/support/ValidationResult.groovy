package pl.wdolata.simpleorderservice.support

import org.apache.kafka.clients.consumer.ConsumerRecord

class ValidationResult {
    String expectedJson
    String expectedKey
    Map<String, String> expectedHeaders
    long expectedTimes
    List<Match> matches

    long getMatchCount() {
        return matches.count { it.fullMatch } as long
    }

    boolean hasExpectedMatchCount() {
        return matchCount == expectedTimes
    }

    /** Readable diff of what was expected against every message that actually arrived. */
    String getDetailedErrorMessage() {
        def sb = new StringBuilder("Kafka assertion failed: expected $expectedTimes matching message(s), " +
            "found $matchCount out of ${matches.size()} received.\n")
        sb << "Expected key: ${expectedKey ?: '<any>'}\n"
        sb << "Expected headers: ${expectedHeaders ?: '<any>'}\n"
        sb << "Expected payload: $expectedJson\n"
        matches.eachWithIndex { match, i ->
            sb << "\nMessage #${i + 1}: key=${match.record.key()} (matched: ${match.keyMatched}), " +
                "headers matched: ${match.headersMatched}, payload matched: ${match.contentMatched}\n"
            sb << "  payload: ${match.record.value()}\n"
            match.differences.each { sb << "  - $it\n" }
        }
        return sb.toString()
    }

    static class Match {
        ConsumerRecord<String, String> record
        boolean keyMatched
        boolean headersMatched
        boolean contentMatched
        List<String> differences = []

        boolean isFullMatch() {
            return keyMatched && headersMatched && contentMatched
        }
    }

}

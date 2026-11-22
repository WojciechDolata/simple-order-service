package pl.wdolata.simpleorderservice.support

import net.javacrumbs.jsonunit.core.Configuration
import net.javacrumbs.jsonunit.core.Option
import net.javacrumbs.jsonunit.core.internal.Diff
import org.apache.kafka.clients.consumer.ConsumerRecord

class MessageValidator {

    static ValidationResult validate(List<ConsumerRecord<String, String>> records, String expectedJson,
                                     String expectedKey, Map<String, String> expectedHeaders,
                                     long expectedTimes, Configuration configuration) {
        def matches = records.collect { record ->
            def match = new ValidationResult.Match(record: record)
            match.keyMatched = expectedKey == null || record.key() == expectedKey

            def actualHeaders = record.headers().collectEntries { [(it.key()): new String(it.value())] }
            match.headersMatched = expectedHeaders == null || actualHeaders.entrySet().containsAll(expectedHeaders.entrySet())

            def diff = Diff.create(expectedJson, record.value(), 'fullJson', '', configuration)
            match.contentMatched = diff.similar()
            if (!match.contentMatched) {
                match.differences = diff.differences().toString().readLines()
            }
            return match
        }
        return new ValidationResult(expectedJson: expectedJson, expectedKey: expectedKey,
            expectedHeaders: expectedHeaders, expectedTimes: expectedTimes, matches: matches)
    }

    static Configuration defaultConfiguration() {
        return Configuration.empty().withOptions(Option.TREATING_NULL_AS_ABSENT)
    }

}

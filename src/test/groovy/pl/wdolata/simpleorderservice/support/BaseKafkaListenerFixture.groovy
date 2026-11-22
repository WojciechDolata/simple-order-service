package pl.wdolata.simpleorderservice.support

import org.opentest4j.AssertionFailedError
import org.awaitility.core.ConditionTimeoutException
import org.springframework.beans.factory.annotation.Value

/** Base for fixtures that watch one topic. Subclasses only say which topic. */
abstract class BaseKafkaListenerFixture {

    @Value('${spring.kafka.bootstrap-servers}')
    protected String bootstrapServers

    private SimpleKafkaListener listener

    protected abstract String getTopic()

    void setup() {
        if (listener == null) {
            listener = new SimpleKafkaListener(bootstrapServers, topic)
        } else {
            listener.cleanup()
        }
    }

    void cleanup() {
        listener?.cleanup()
    }

    void assertMessagePublished(String json, String key = null, Map<String, String> headers = null,
                                int timeoutMillis = 5_000, long times = 1) {
        ValidationResult result = null
        try {
            WaitUtils.waitUntil(timeoutMillis) {
                result = listener.validate(json, key, headers, times, MessageValidator.defaultConfiguration())
                assert result.hasExpectedMatchCount()
            }
        } catch (ConditionTimeoutException ignored) {
            throw new AssertionFailedError(result.detailedErrorMessage)
        }
    }

    /** A negative assertion has to wait: a late message would otherwise slip through unnoticed. */
    void assertNothingPublished(int millis = 1_000) {
        WaitUtils.assertHoldsFor(millis) {
            assert listener.messageCount() == 0
        }
    }

}

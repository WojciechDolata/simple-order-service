package pl.wdolata.simpleorderservice.support

import java.time.Duration
import java.util.concurrent.TimeUnit

import org.awaitility.Awaitility
import org.awaitility.core.ThrowingRunnable

class WaitUtils {

    /** Retries the assertion until it passes or the timeout elapses. */
    static void waitUntil(int millis = 5_000, int pollIntervalMillis = 10, ThrowingRunnable assertion) {
        Awaitility.await()
            .pollInterval(pollIntervalMillis, TimeUnit.MILLISECONDS)
            .atMost(millis, TimeUnit.MILLISECONDS)
            .untilAsserted(assertion)
    }

    /** Asserts that the condition keeps holding for the whole period, i.e. "nothing happened". */
    static void assertHoldsFor(int millis, ThrowingRunnable assertion) {
        Awaitility.await()
            .pollInterval(50, TimeUnit.MILLISECONDS)
            .during(Duration.ofMillis(millis))
            .atMost(Duration.ofMillis(millis + 2_000))
            .untilAsserted(assertion)
    }

}

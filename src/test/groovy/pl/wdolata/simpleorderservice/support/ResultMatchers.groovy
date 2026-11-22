package pl.wdolata.simpleorderservice.support

import net.javacrumbs.jsonunit.core.Configuration
import net.javacrumbs.jsonunit.core.Option
import net.javacrumbs.jsonunit.core.internal.Diff
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.ResultMatcher

class ResultMatchers {

    /**
     * Whole-body JSON comparison (no extra fields allowed, null equals absent).
     * A new field in the response is a conscious decision, not a silent leak.
     */
    static ResultMatcher contentJsonEquals(String expected) {
        return { MvcResult result ->
            def diff = Diff.create(expected, result.response.contentAsString, 'fullJson', '',
                Configuration.empty().withOptions(Option.TREATING_NULL_AS_ABSENT))
            if (!diff.similar()) {
                throw new AssertionError("Response body differs:\n${diff.differences()}\n" +
                    "Actual: ${result.response.contentAsString}")
            }
        } as ResultMatcher
    }

}

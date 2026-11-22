package pl.wdolata.simpleorderservice.support

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import static com.github.tomakehurst.wiremock.client.WireMock.request
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import com.github.tomakehurst.wiremock.http.RequestMethod
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder

/** Small fluent wrapper over a WireMock stub: what we expect to receive and what we answer. */
class MockRequest {

    final MappingBuilder mapping
    final RequestPatternBuilder pattern
    final ResponseDefinitionBuilder response = aResponse()

    private MockRequest(String method, String path) {
        this.mapping = request(method, urlPathEqualTo(path))
        this.pattern = new RequestPatternBuilder(RequestMethod.fromString(method),
            urlPathEqualTo(path))
    }

    static MockRequest getRequest(String path) {
        return new MockRequest('GET', path)
    }

    static MockRequest postRequest(String path) {
        return new MockRequest('POST', path)
    }

    MockRequest withJsonRequestBody(String json) {
        mapping.withRequestBody(equalToJson(json, true, true))
        pattern.withRequestBody(equalToJson(json, true, true))
        return this
    }

    MockRequest willReturn(int status, String body = null) {
        response.withStatus(status)
        if (body != null) {
            response.withHeader('Content-Type', 'application/json').withBody(body)
        }
        return this
    }

    MockRequest withDelay(int millis) {
        response.withFixedDelay(millis)
        return this
    }

}

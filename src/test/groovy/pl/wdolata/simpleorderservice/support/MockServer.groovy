package pl.wdolata.simpleorderservice.support

import static com.github.tomakehurst.wiremock.client.WireMock.anyRequestedFor
import static com.github.tomakehurst.wiremock.client.WireMock.anyUrl
import static com.github.tomakehurst.wiremock.client.WireMock.exactly

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration

/** A real HTTP server standing in for a dependency we do not own. */
class MockServer {

    private final WireMockServer server = new WireMockServer(WireMockConfiguration.options().dynamicPort())

    MockServer() {
        server.start()
    }

    String baseUrl() {
        return server.baseUrl()
    }

    void reset() {
        server.resetAll()
    }

    void expectRequest(MockRequest request) {
        server.givenThat(request.mapping.willReturn(request.response))
    }

    void verifyRequest(int count, MockRequest request) {
        server.verify(exactly(count), request.pattern)
    }

    void verifyNoRequests() {
        server.verify(0, anyRequestedFor(anyUrl()))
    }

}

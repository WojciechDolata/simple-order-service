package pl.wdolata.simpleorderservice.fixtures

import static pl.wdolata.simpleorderservice.support.MockRequest.getRequest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import pl.wdolata.simpleorderservice.fixtures.requests.CouponRequests
import pl.wdolata.simpleorderservice.support.MockRequest
import pl.wdolata.simpleorderservice.support.MockServer

/** The external coupon service, as seen from our side of the wire. */
@Component
class CouponServiceFixture {

    @Autowired
    private MockServer mockServer

    void reset() {
        mockServer.reset()
    }

    MockRequest stubCoupon(String code = 'SAVE10', String responseBody = CouponRequests.activeCouponResponseBody()) {
        return stub(code, 200, responseBody)
    }

    MockRequest stubExpiredCoupon(String code = 'OLD2020') {
        return stub(code, 200, CouponRequests.expiredCouponResponseBody { it['code'] = code })
    }

    MockRequest stubUnknownCoupon(String code) {
        return stub(code, 404, null)
    }

    MockRequest stubUnavailable(String code, int statusCode = 503) {
        return stub(code, statusCode, null)
    }

    MockRequest stubSlowResponse(String code, int delayMillis) {
        return stub(code, 200, CouponRequests.activeCouponResponseBody { it['code'] = code }, delayMillis)
    }

    void verifyRequested(MockRequest request, int times = 1) {
        mockServer.verifyRequest(times, request)
    }

    void verifyNoRequests() {
        mockServer.verifyNoRequests()
    }

    private MockRequest stub(String code, int status, String body, int delayMillis = 0) {
        def request = getRequest("/coupons/$code").willReturn(status, body)
        if (delayMillis > 0) {
            request.withDelay(delayMillis)
        }
        mockServer.expectRequest(request)
        return request
    }

}

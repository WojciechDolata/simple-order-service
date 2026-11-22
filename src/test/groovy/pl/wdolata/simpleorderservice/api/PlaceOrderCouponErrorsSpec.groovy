package pl.wdolata.simpleorderservice.api

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static pl.wdolata.simpleorderservice.support.ResultMatchers.contentJsonEquals

import pl.wdolata.simpleorderservice.IntegrationSpec
import pl.wdolata.simpleorderservice.fixtures.requests.OrderRequests

class PlaceOrderCouponErrorsSpec extends IntegrationSpec {

    def 'should reject expired coupon and leave no trace'() {
        given: 'the coupon service says the coupon expired in the past'
        couponService.stubExpiredCoupon('OLD2020')

        when: 'the customer places an order with the expired coupon'
        def result = api.placeOrder(OrderRequests.WithCoupon.requestBody { it['couponCode'] = 'OLD2020' })

        then: 'the request is rejected'
        result.andExpectAll(
            status().isUnprocessableContent(),
            contentJsonEquals(OrderRequests.couponExpiredResponseBody())
        )

        and: 'nothing is stored'
        orders.count() == 0

        and: 'nothing is published'
        orderEvents.assertNothingPublished()
    }

    def 'should reject unknown coupon and leave no trace'() {
        given: 'the coupon service does not know the coupon'
        couponService.stubUnknownCoupon('NOPE')

        when: 'the customer places an order with the unknown coupon'
        def result = api.placeOrder(OrderRequests.WithCoupon.requestBody { it['couponCode'] = 'NOPE' })

        then: 'the request is rejected'
        result.andExpectAll(
            status().isUnprocessableContent(),
            contentJsonEquals(OrderRequests.couponNotFoundResponseBody())
        )

        and: 'nothing is stored or published'
        orders.count() == 0
        orderEvents.assertNothingPublished()
    }

    def 'should answer 502 and leave no trace when the coupon service #caseName'() {
        given: 'the coupon service is failing or too slow'
        stub(couponService)

        when: 'the customer places an order with the coupon'
        def result = api.placeOrder(OrderRequests.WithCoupon.requestBody { it['couponCode'] = 'SLOW' })

        then: 'the request is answered with 502'
        result.andExpectAll(
            status().isBadGateway(),
            contentJsonEquals(OrderRequests.couponServiceUnavailableResponseBody())
        )

        and: 'nothing is stored or published'
        orders.count() == 0
        orderEvents.assertNothingPublished()

        where:
        caseName               | stub
        'returns 503'          | { it.stubUnavailable('SLOW', 503) }
        'returns 500'          | { it.stubUnavailable('SLOW', 500) }
        'responds too slowly'  | { it.stubSlowResponse('SLOW', 1_500) }
    }

}

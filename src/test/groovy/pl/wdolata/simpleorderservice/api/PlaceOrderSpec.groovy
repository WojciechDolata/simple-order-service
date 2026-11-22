package pl.wdolata.simpleorderservice.api

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static pl.wdolata.simpleorderservice.support.ResultMatchers.contentJsonEquals

import pl.wdolata.simpleorderservice.IntegrationSpec
import pl.wdolata.simpleorderservice.fixtures.entities.OrderRows
import pl.wdolata.simpleorderservice.fixtures.requests.CouponRequests
import pl.wdolata.simpleorderservice.fixtures.requests.EventMessages
import pl.wdolata.simpleorderservice.fixtures.requests.OrderRequests

class PlaceOrderSpec extends IntegrationSpec {

    def 'should place order with coupon'() {
        given: 'the coupon exists in the coupon service'
        def couponRequest = couponService.stubCoupon('SAVE10', CouponRequests.activeCouponResponseBody())

        when: 'the customer places the order'
        def result = api.placeOrder(OrderRequests.WithCoupon.requestBody())

        then: 'the order is accepted with the discounted total'
        result.andExpectAll(
            status().isCreated(),
            contentJsonEquals(OrderRequests.WithCoupon.responseBody())
        )

        and: 'the coupon service was asked about the coupon exactly once'
        couponService.verifyRequested(couponRequest)

        and: 'the order is stored'
        orders.findAll() == [OrderRows.placedWithCoupon()]

        and: 'OrderPlaced is published, keyed by order id'
        orderEvents.assertMessagePublished(
            EventMessages.OrderPlaced.withCoupon(),
            ORDER_ID.toString()
        )
    }

    def 'should place order without coupon without calling the coupon service'() {
        when: 'the customer places an order without a coupon'
        def result = api.placeOrder(OrderRequests.WithoutCoupon.requestBody())

        then: 'full price'
        result.andExpectAll(
            status().isCreated(),
            contentJsonEquals(OrderRequests.WithoutCoupon.responseBody())
        )

        and: 'the coupon service was not involved'
        couponService.verifyNoRequests()

        and: 'the order is stored'
        orders.findAll() == [OrderRows.placedWithoutCoupon()]

        and: 'OrderPlaced is published'
        orderEvents.assertMessagePublished(
            EventMessages.OrderPlaced.withoutCoupon(),
            ORDER_ID.toString()
        )
    }

}

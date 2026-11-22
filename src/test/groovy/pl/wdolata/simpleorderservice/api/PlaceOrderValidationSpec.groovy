package pl.wdolata.simpleorderservice.api

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import pl.wdolata.simpleorderservice.IntegrationSpec
import pl.wdolata.simpleorderservice.fixtures.requests.OrderRequests

class PlaceOrderValidationSpec extends IntegrationSpec {

    def 'should reject invalid order when #caseName'() {
        given: 'a valid order request broken in one place'
        def body = OrderRequests.WithCoupon.requestBody(modification)

        expect: 'the request is rejected before anything else happens'
        api.placeOrder(body)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath('$.errorCode').value('INVALID_REQUEST'))

        and: 'the coupon service is not called, nothing is stored or published'
        couponService.verifyNoRequests()
        orders.count() == 0
        orderEvents.assertNothingPublished(300)

        where:
        caseName                  | modification
        'user id is missing'      | { it.remove('userId') }
        'user id is not a uuid'   | { it['userId'] = 'not-a-uuid' }
        'items are empty'         | { it['items'] = [] }
        'items are missing'       | { it.remove('items') }
        'sku is blank'            | { it['items'][0]['sku'] = ' ' }
        'quantity is zero'        | { it['items'][0]['quantity'] = 0 }
        'quantity is negative'    | { it['items'][0]['quantity'] = -1 }
        'unit price is zero'      | { it['items'][0]['unitPrice'] = 0 }
        'unit price is negative'  | { it['items'][0]['unitPrice'] = -5.00 }
        'currency is unsupported' | { it['currency'] = 'CZK' }
        'coupon code is blank'    | { it['couponCode'] = ' ' }
    }

    def 'should reject malformed json'() {
        expect: 'a malformed json body is sent'
        api.placeOrder('{ not json')
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath('$.errorCode').value('INVALID_REQUEST'))

        and: 'nothing is stored'
        orders.count() == 0
    }

}

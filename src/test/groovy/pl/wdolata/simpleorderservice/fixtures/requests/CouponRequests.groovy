package pl.wdolata.simpleorderservice.fixtures.requests

import static pl.wdolata.simpleorderservice.support.JsonUtils.parseJsonFile

class CouponRequests {

    static String activeCouponResponseBody(Closure modifications = {}) {
        return parseJsonFile('coupon/active_coupon_response.json', modifications)
    }

    static String expiredCouponResponseBody(Closure modifications = {}) {
        return parseJsonFile('coupon/expired_coupon_response.json', modifications)
    }

}

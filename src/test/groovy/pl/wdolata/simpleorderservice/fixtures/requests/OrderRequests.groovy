package pl.wdolata.simpleorderservice.fixtures.requests

import static pl.wdolata.simpleorderservice.support.JsonUtils.parseJsonFile

@SuppressWarnings('EmptyClass')
class OrderRequests {

    static String couponExpiredResponseBody(Closure modifications = {}) {
        return parseJsonFile('api/coupon_expired_response.json', modifications)
    }

    static String couponNotFoundResponseBody(Closure modifications = {}) {
        return parseJsonFile('api/coupon_not_found_response.json', modifications)
    }

    static String couponServiceUnavailableResponseBody() {
        return parseJsonFile('api/coupon_service_unavailable_response.json')
    }

    static class WithCoupon {
        static String requestBody(Closure modifications = {}) {
            return parseJsonFile('api/place_order_with_coupon_request.json', modifications)
        }

        static String responseBody(Closure modifications = {}) {
            return parseJsonFile('api/place_order_with_coupon_response.json', modifications)
        }
    }

    static class WithoutCoupon {
        static String requestBody(Closure modifications = {}) {
            return parseJsonFile('api/place_order_without_coupon_request.json', modifications)
        }

        static String responseBody(Closure modifications = {}) {
            return parseJsonFile('api/place_order_without_coupon_response.json', modifications)
        }
    }

}

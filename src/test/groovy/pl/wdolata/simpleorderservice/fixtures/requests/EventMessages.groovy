package pl.wdolata.simpleorderservice.fixtures.requests

import static pl.wdolata.simpleorderservice.support.JsonUtils.parseJsonFile

class EventMessages {

    static class OrderPlaced {
        static String withCoupon(Closure modifications = {}) {
            return parseJsonFile('events/order_placed_with_coupon.json', modifications)
        }

        static String withoutCoupon(Closure modifications = {}) {
            return parseJsonFile('events/order_placed_without_coupon.json', modifications)
        }
    }

}

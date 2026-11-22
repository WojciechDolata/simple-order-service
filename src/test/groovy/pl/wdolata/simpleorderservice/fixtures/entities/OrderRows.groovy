package pl.wdolata.simpleorderservice.fixtures.entities

import java.time.Instant

import pl.wdolata.simpleorderservice.IntegrationSpec

/** Expected rows of the orders table, comparable with Orders.findAll(). */
class OrderRows {

    private static final List<Map> ITEMS = [
        [sku: 'BOOK-1', quantity: 2, unitPrice: 30.00],
        [sku: 'PEN-7', quantity: 1, unitPrice: 40.00]
    ]

    static Map placedWithCoupon(Map overrides = [:]) {
        return [
            id        : IntegrationSpec.ORDER_ID,
            userId    : IntegrationSpec.USER_ID,
            couponCode: 'SAVE10',
            currency  : 'PLN',
            subtotal  : 100.00,
            total     : 90.00,
            items     : ITEMS,
            createdAt : IntegrationSpec.NOW
        ] + overrides
    }

    static Map placedWithoutCoupon() {
        return placedWithCoupon(couponCode: null, total: 100.00)
    }

}

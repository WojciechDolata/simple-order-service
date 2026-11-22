package pl.wdolata.simpleorderservice.fixtures.entities

import pl.wdolata.simpleorderservice.IntegrationSpec
import pl.wdolata.simpleorderservice.domain.model.Order
import pl.wdolata.simpleorderservice.domain.model.OrderItem
import pl.wdolata.simpleorderservice.domain.model.valueobject.CouponCode
import pl.wdolata.simpleorderservice.domain.model.valueobject.OrderId
import pl.wdolata.simpleorderservice.domain.model.valueobject.UserId

/** Domain-side shape of an order, matching OrderEntities one to one. */
class OrderModels {

    static List<OrderItem> items() {
        return [new OrderItem('BOOK-1', 2, 30.00G), new OrderItem('PEN-7', 1, 40.00G)]
    }

    static Order placedWithCoupon() {
        return new Order(new OrderId(IntegrationSpec.ORDER_ID), new UserId(IntegrationSpec.USER_ID), 'PLN', items(), 100.00G, 90.00G,
            new CouponCode('SAVE10'), IntegrationSpec.NOW)
    }

    static Order placedWithoutCoupon() {
        return new Order(new OrderId(IntegrationSpec.ORDER_ID), new UserId(IntegrationSpec.USER_ID), 'PLN', items(), 100.00G, 100.00G,
            null, IntegrationSpec.NOW)
    }

}

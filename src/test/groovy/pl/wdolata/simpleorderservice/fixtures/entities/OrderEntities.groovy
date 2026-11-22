package pl.wdolata.simpleorderservice.fixtures.entities

import pl.wdolata.simpleorderservice.IntegrationSpec
import pl.wdolata.simpleorderservice.adapters.database.orders.OrderEntity
import pl.wdolata.simpleorderservice.adapters.database.orders.model.OrderItemData
import pl.wdolata.simpleorderservice.adapters.database.orders.model.OrderItemsData

/** Database-side shape of an order: what the adapters.database layer reads and writes. */
class OrderEntities {

    static OrderItemsData items() {
        return new OrderItemsData([
            new OrderItemData('BOOK-1', 2, 30.00G),
            new OrderItemData('PEN-7', 1, 40.00G)
        ])
    }

    static OrderEntity placedWithCoupon(Closure<OrderEntity.OrderEntityBuilder> customize = { it }) {
        def builder = OrderEntity.builder()
            .id(IntegrationSpec.ORDER_ID)
            .userId(IntegrationSpec.USER_ID)
            .couponCode('SAVE10')
            .currency('PLN')
            .subtotal(100.00G)
            .total(90.00G)
            .items(items())
            .createdAt(IntegrationSpec.NOW)
        return customize.call(builder).build()
    }

    static OrderEntity placedWithoutCoupon() {
        return placedWithCoupon { it.couponCode(null).total(100.00G) }
    }

}

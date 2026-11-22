package pl.wdolata.simpleorderservice.adapters.database.orders

import org.assertj.core.api.Assertions
import org.springframework.dao.DuplicateKeyException
import pl.wdolata.simpleorderservice.IntegrationSpec
import pl.wdolata.simpleorderservice.fixtures.entities.OrderEntities

/** The repository against the real schema: converters, column mapping and insert-only semantics. */
class OrderPersistenceSpec extends IntegrationSpec {

    def 'should store and reload an order with its items from the jsonb column'() {
        given:
        def entity = OrderEntities.placedWithCoupon()

        when:
        orders.save(entity)
        def reloaded = orders.findEntity(ORDER_ID).orElseThrow()

        then:
        Assertions.assertThat(reloaded).usingRecursiveComparison().isEqualTo(entity)
        reloaded.items.items().size() == 2
    }

    def 'should store an order without a coupon code as null'() {
        when:
        orders.save(OrderEntities.placedWithoutCoupon())

        then:
        orders.findEntity(ORDER_ID).orElseThrow().couponCode == null
    }

    def 'should refuse to store a second order with the same id instead of overwriting it'() {
        given:
        orders.save(OrderEntities.placedWithCoupon())

        when:
        orders.save(OrderEntities.placedWithCoupon { it.total(1.00G) })

        then:
        thrown(DuplicateKeyException)
        orders.findEntity(ORDER_ID).orElseThrow().total == 90.00G
    }

}

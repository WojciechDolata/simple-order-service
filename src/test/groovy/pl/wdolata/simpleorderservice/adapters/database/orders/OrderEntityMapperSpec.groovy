package pl.wdolata.simpleorderservice.adapters.database.orders

import pl.wdolata.simpleorderservice.adapters.database.orders.mapping.OrderEntityMapperImpl
import pl.wdolata.simpleorderservice.fixtures.entities.OrderEntities
import pl.wdolata.simpleorderservice.fixtures.entities.OrderModels
import spock.lang.Specification

class OrderEntityMapperSpec extends Specification {

    def mapper = new OrderEntityMapperImpl()

    def 'should not lose information when mapping back and forth #caseName'() {
        expect:
        mapper.toDomain(mapper.toEntity(order)) == order

        where:
        caseName         | order
        'order a coupon' | OrderModels.placedWithCoupon()
        'order no coupon' | OrderModels.placedWithoutCoupon()
    }

    def 'should map placedAt to createdAt and unwrap value objects'() {
        when:
        def entity = mapper.toEntity(OrderModels.placedWithCoupon())

        then:
        entity.id == OrderModels.placedWithCoupon().id().value()
        entity.userId == OrderModels.placedWithCoupon().userId().value()
        entity.couponCode == 'SAVE10'
        entity.createdAt == OrderModels.placedWithCoupon().placedAt()
        entity.items.items().collect { it.sku() } == ['BOOK-1', 'PEN-7']
    }

    def 'should leave coupon code empty when the entity has none'() {
        expect:
        mapper.toDomain(OrderEntities.placedWithoutCoupon()).couponCode() == null
    }

}

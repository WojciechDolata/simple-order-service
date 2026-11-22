package pl.wdolata.simpleorderservice.adapters.database.orders

import org.springframework.dao.DuplicateKeyException
import pl.wdolata.simpleorderservice.adapters.database.orders.mapping.OrderEntityMapperImpl
import pl.wdolata.simpleorderservice.fixtures.entities.OrderModels
import spock.lang.Specification

/** The store in isolation: the repository is mocked, the mapper is the real one. */
class OrderStoreImplSpec extends Specification {

    def repository = Mock(OrderRepository)
    def store = new OrderStoreImpl(repository, new OrderEntityMapperImpl())

    def 'should save the mapped entity and return the stored order'() {
        given:
        def order = OrderModels.placedWithCoupon()

        when:
        def result = store.save(order)

        then:
        1 * repository.save({
            it.id == order.id().value() && it.total == 90.00G && it.createdAt == order.placedAt()
        }) >> { it[0] }

        and:
        result == order
    }

    def 'should return the state reported by the repository, not the input'() {
        given:
        def stored = OrderModels.placedWithoutCoupon()

        when:
        def result = store.save(OrderModels.placedWithCoupon())

        then:
        1 * repository.save(_) >> new OrderEntityMapperImpl().toEntity(stored)

        and:
        result == stored
    }

    def 'should propagate repository failures'() {
        when:
        store.save(OrderModels.placedWithCoupon())

        then:
        1 * repository.save(_) >> { throw new DuplicateKeyException('duplicate order id') }
        thrown(DuplicateKeyException)
    }

}

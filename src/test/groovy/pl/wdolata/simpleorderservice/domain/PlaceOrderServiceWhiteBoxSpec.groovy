package pl.wdolata.simpleorderservice.domain

import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset

import pl.wdolata.simpleorderservice.domain.commands.PlaceOrder
import pl.wdolata.simpleorderservice.domain.model.Coupon
import pl.wdolata.simpleorderservice.domain.model.OrderItem
import pl.wdolata.simpleorderservice.domain.model.valueobject.CouponCode
import pl.wdolata.simpleorderservice.domain.model.valueobject.OrderId
import pl.wdolata.simpleorderservice.domain.model.valueobject.UserId
import pl.wdolata.simpleorderservice.domain.ports.CouponProvider
import pl.wdolata.simpleorderservice.domain.ports.OrderEventStream
import pl.wdolata.simpleorderservice.domain.ports.OrderStore
import spock.lang.Specification

/**
 * The same behaviour as PlaceOrderSpec, tested "from the inside": every port is mocked and the test
 * asserts on who calls whom. It reads reasonably, and it is exactly what a refactor breaks.
 */
class PlaceOrderServiceWhiteBoxSpec extends Specification {

    static final UUID ORDER_ID = UUID.fromString('0b1c2d3e-4f50-4a61-8b72-93a4b5c6d7e8')
    static final Instant NOW = Instant.parse('2026-10-21T10:00:00Z')
    static final CouponCode SAVE10 = new CouponCode('SAVE10')
    static final UserId USER_ID = new UserId(UUID.fromString('7a1f4c2e-9b3d-4e58-a6c0-5d2e8f1b3a94'))

    def couponProvider = Mock(CouponProvider)
    def orderStore = Mock(OrderStore)
    def orderEventStream = Mock(OrderEventStream)
    def orderIdGenerator = Stub(OrderIdGenerator) { generate() >> new OrderId(ORDER_ID) }
    def clock = Clock.fixed(NOW, ZoneOffset.UTC)

    def service = new PlaceOrderService(couponProvider, orderStore, orderEventStream, orderIdGenerator, clock)

    def items = [new OrderItem('BOOK-1', 2, 30.00G), new OrderItem('PEN-7', 1, 40.00G)]

    def 'should place order with coupon'() {
        when:
        def order = service.placeOrder(new PlaceOrder(USER_ID, items, 'PLN', SAVE10))

        then:
        1 * couponProvider.findCoupon(SAVE10) >> Optional.of(new Coupon(SAVE10, 10, Instant.parse('2027-01-01T00:00:00Z')))
        1 * orderStore.save({ it.total == 90.00G && it.couponCode == SAVE10 && it.userId() == USER_ID }) >> { it[0] }
        1 * orderEventStream.publish({ it.orderId == new OrderId(ORDER_ID) && it.userId == USER_ID && it.total == 90.00G })

        and:
        order.total() == 90.00G
    }

    def 'should place order without coupon'() {
        when:
        def order = service.placeOrder(new PlaceOrder(USER_ID, items, 'PLN', null))

        then:
        0 * couponProvider._
        1 * orderStore.save({ it.total == 100.00G }) >> { it[0] }
        1 * orderEventStream.publish({ it.total == 100.00G })

        and:
        order.total() == 100.00G
    }

}

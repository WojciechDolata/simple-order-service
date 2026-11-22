package pl.wdolata.simpleorderservice.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.Instant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.wdolata.simpleorderservice.domain.commands.PlaceOrder;
import pl.wdolata.simpleorderservice.domain.exception.CouponExpiredException;
import pl.wdolata.simpleorderservice.domain.exception.CouponNotFoundException;
import pl.wdolata.simpleorderservice.domain.model.Order;
import pl.wdolata.simpleorderservice.domain.model.OrderItem;
import pl.wdolata.simpleorderservice.domain.model.event.OrderPlacedEvent;
import pl.wdolata.simpleorderservice.domain.ports.CouponProvider;
import pl.wdolata.simpleorderservice.domain.ports.OrderEventStream;
import pl.wdolata.simpleorderservice.domain.ports.OrderStore;

/**
 * Deliberately "fat": checks the coupon, computes the discount, stores the order and
 * publishes the event in one place, which makes it the natural target of the demo refactor.
 */
@Service
@RequiredArgsConstructor
public class PlaceOrderService {
    private static final String DEFAULT_CURRENCY = "PLN";

    private final CouponProvider couponProvider;
    private final OrderStore orderStore;
    private final OrderEventStream orderEventStream;
    private final OrderIdGenerator orderIdGenerator;
    private final Clock clock;

    @Transactional
    public Order placeOrder(PlaceOrder command) {
        var now = Instant.now(clock);
        var currency = command.currency() != null ? command.currency() : DEFAULT_CURRENCY;

        var subtotal = command.items().stream()
            .map(OrderItem::lineTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        var total = subtotal;
        if (command.couponCode() != null) {
            var coupon = couponProvider.findCoupon(command.couponCode())
                .orElseThrow(() -> new CouponNotFoundException(command.couponCode()));
            if (coupon.isExpiredAt(now)) {
                throw new CouponExpiredException(coupon.code());
            }
            total = subtotal
                .multiply(BigDecimal.valueOf(100 - coupon.discountPercent()))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        }

        var order = orderStore.save(new Order(
            orderIdGenerator.generate(),
            command.userId(),
            currency,
            command.items(),
            subtotal,
            total,
            command.couponCode(),
            now
        ));

        orderEventStream.publish(new OrderPlacedEvent(
            order.id(),
            order.userId(),
            order.couponCode(),
            order.currency(),
            order.total(),
            now
        ));
        return order;
    }
}

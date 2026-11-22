package pl.wdolata.simpleorderservice.domain.model.event;

import java.math.BigDecimal;
import java.time.Instant;

import org.jspecify.annotations.Nullable;
import pl.wdolata.simpleorderservice.domain.model.valueobject.CouponCode;
import pl.wdolata.simpleorderservice.domain.model.valueobject.OrderId;
import pl.wdolata.simpleorderservice.domain.model.valueobject.UserId;

public record OrderPlacedEvent(
    OrderId orderId,
    UserId userId,
    @Nullable CouponCode couponCode,
    String currency,
    BigDecimal total,
    Instant occurredAt
) {
}

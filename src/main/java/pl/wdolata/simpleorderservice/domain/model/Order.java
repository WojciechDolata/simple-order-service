package pl.wdolata.simpleorderservice.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import org.jspecify.annotations.Nullable;
import pl.wdolata.simpleorderservice.domain.model.valueobject.CouponCode;
import pl.wdolata.simpleorderservice.domain.model.valueobject.OrderId;
import pl.wdolata.simpleorderservice.domain.model.valueobject.UserId;

public record Order(
    OrderId id,
    UserId userId,
    String currency,
    List<OrderItem> items,
    BigDecimal subtotal,
    BigDecimal total,
    @Nullable CouponCode couponCode,
    Instant placedAt
) {
}

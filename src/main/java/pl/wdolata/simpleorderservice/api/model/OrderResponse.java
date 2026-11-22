package pl.wdolata.simpleorderservice.api.model;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderResponse(
    UUID orderId,
    UUID userId,
    String status,
    String currency,
    BigDecimal subtotal,
    BigDecimal total,
    String couponCode
) {
}

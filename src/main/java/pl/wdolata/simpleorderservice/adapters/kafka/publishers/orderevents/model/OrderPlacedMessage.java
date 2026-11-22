package pl.wdolata.simpleorderservice.adapters.kafka.publishers.orderevents.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderPlacedMessage(
    UUID orderId,
    UUID userId,
    String couponCode,
    String currency,
    BigDecimal total,
    Instant occurredAt
) {
}

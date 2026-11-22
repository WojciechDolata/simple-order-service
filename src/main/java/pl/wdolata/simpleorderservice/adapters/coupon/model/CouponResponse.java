package pl.wdolata.simpleorderservice.adapters.coupon.model;

import java.time.Instant;

public record CouponResponse(String code, int discountPercent, Instant expiresAt) {
}

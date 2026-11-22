package pl.wdolata.simpleorderservice.domain.model;

import java.time.Instant;

import pl.wdolata.simpleorderservice.domain.model.valueobject.CouponCode;

public record Coupon(CouponCode code, int discountPercent, Instant expiresAt) {

    public boolean isExpiredAt(Instant moment) {
        return expiresAt.isBefore(moment);
    }
}

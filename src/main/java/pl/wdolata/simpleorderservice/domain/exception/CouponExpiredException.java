package pl.wdolata.simpleorderservice.domain.exception;

import pl.wdolata.simpleorderservice.domain.model.valueobject.CouponCode;

public class CouponExpiredException extends RuntimeException {
    private static final String MESSAGE = "Coupon %s has expired";

    public CouponExpiredException(CouponCode code) {
        super(MESSAGE.formatted(code.value()));
    }
}

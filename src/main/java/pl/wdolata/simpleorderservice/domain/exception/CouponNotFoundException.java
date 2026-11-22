package pl.wdolata.simpleorderservice.domain.exception;

import pl.wdolata.simpleorderservice.domain.model.valueobject.CouponCode;

public class CouponNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Coupon %s does not exist";

    public CouponNotFoundException(CouponCode code) {
        super(MESSAGE.formatted(code.value()));
    }
}

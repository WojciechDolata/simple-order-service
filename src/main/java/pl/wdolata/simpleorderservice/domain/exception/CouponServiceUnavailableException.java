package pl.wdolata.simpleorderservice.domain.exception;

public class CouponServiceUnavailableException extends RuntimeException {
    private static final String MESSAGE = "Coupon service is unavailable";

    public CouponServiceUnavailableException(Throwable cause) {
        super(MESSAGE, cause);
    }
}

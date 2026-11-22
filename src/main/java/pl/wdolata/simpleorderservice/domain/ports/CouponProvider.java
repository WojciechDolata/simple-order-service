package pl.wdolata.simpleorderservice.domain.ports;

import java.util.Optional;

import pl.wdolata.simpleorderservice.domain.model.Coupon;
import pl.wdolata.simpleorderservice.domain.model.valueobject.CouponCode;

public interface CouponProvider {
    Optional<Coupon> findCoupon(CouponCode code);
}

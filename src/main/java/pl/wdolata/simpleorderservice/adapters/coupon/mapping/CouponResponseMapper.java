package pl.wdolata.simpleorderservice.adapters.coupon.mapping;

import org.mapstruct.Mapper;
import pl.wdolata.simpleorderservice.adapters.coupon.model.CouponResponse;
import pl.wdolata.simpleorderservice.domain.model.Coupon;
import pl.wdolata.simpleorderservice.domain.model.valueobject.CouponCode;

@Mapper
public interface CouponResponseMapper {

    Coupon toDomain(CouponResponse response);

    default CouponCode toCouponCode(String code) {
        return new CouponCode(code);
    }
}

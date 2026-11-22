package pl.wdolata.simpleorderservice.adapters.coupon;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import pl.wdolata.simpleorderservice.adapters.coupon.mapping.CouponResponseMapper;
import pl.wdolata.simpleorderservice.adapters.coupon.model.CouponResponse;
import pl.wdolata.simpleorderservice.domain.exception.CouponServiceUnavailableException;
import pl.wdolata.simpleorderservice.domain.model.Coupon;
import pl.wdolata.simpleorderservice.domain.model.valueobject.CouponCode;
import pl.wdolata.simpleorderservice.domain.ports.CouponProvider;

@Component
public class CouponProviderImpl implements CouponProvider {
    private static final String COUPON_PATH = "/coupons/{code}";

    private final RestClient restClient;
    private final CouponResponseMapper mapper;

    public CouponProviderImpl(@Qualifier("couponRestClient") RestClient restClient, CouponResponseMapper mapper) {
        this.restClient = restClient;
        this.mapper = mapper;
    }

    @Override
    public Optional<Coupon> findCoupon(CouponCode code) {
        try {
            var response = restClient.get().uri(COUPON_PATH, code.value()).retrieve().body(CouponResponse.class);
            return Optional.ofNullable(response).map(mapper::toDomain);
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        } catch (RestClientException e) {
            throw new CouponServiceUnavailableException(e);
        }
    }
}

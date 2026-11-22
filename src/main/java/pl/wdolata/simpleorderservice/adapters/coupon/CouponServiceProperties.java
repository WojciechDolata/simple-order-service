package pl.wdolata.simpleorderservice.adapters.coupon;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("coupon-service")
public record CouponServiceProperties(String url, Duration timeout) {
}

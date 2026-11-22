package pl.wdolata.simpleorderservice.domain.commands;

import java.util.List;

import org.jspecify.annotations.Nullable;
import pl.wdolata.simpleorderservice.domain.model.OrderItem;
import pl.wdolata.simpleorderservice.domain.model.valueobject.CouponCode;
import pl.wdolata.simpleorderservice.domain.model.valueobject.UserId;

public record PlaceOrder(UserId userId, List<OrderItem> items, @Nullable String currency, @Nullable CouponCode couponCode) {
}

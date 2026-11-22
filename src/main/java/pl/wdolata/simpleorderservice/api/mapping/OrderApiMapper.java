package pl.wdolata.simpleorderservice.api.mapping;

import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.wdolata.simpleorderservice.api.model.OrderResponse;
import pl.wdolata.simpleorderservice.api.model.PlaceOrderRequest;
import pl.wdolata.simpleorderservice.domain.commands.PlaceOrder;
import pl.wdolata.simpleorderservice.domain.model.Order;
import pl.wdolata.simpleorderservice.domain.model.valueobject.CouponCode;
import pl.wdolata.simpleorderservice.domain.model.valueobject.OrderId;
import pl.wdolata.simpleorderservice.domain.model.valueobject.UserId;

@Mapper
public interface OrderApiMapper {

    PlaceOrder toCommand(PlaceOrderRequest request);

    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "status", constant = "PLACED")
    OrderResponse toResponse(Order order);

    default UUID toUuid(OrderId id) {
        return id.value();
    }

    default UUID toUuid(UserId id) {
        return id.value();
    }

    default UserId toUserId(UUID id) {
        return new UserId(id);
    }

    default String toString(CouponCode code) {
        return code == null ? null : code.value();
    }

    default CouponCode toCouponCode(String code) {
        return code == null ? null : new CouponCode(code);
    }
}

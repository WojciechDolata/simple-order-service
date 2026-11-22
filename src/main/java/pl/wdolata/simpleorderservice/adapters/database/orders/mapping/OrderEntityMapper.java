package pl.wdolata.simpleorderservice.adapters.database.orders.mapping;

import java.util.List;
import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.wdolata.simpleorderservice.adapters.database.orders.OrderEntity;
import pl.wdolata.simpleorderservice.adapters.database.orders.model.OrderItemData;
import pl.wdolata.simpleorderservice.adapters.database.orders.model.OrderItemsData;
import pl.wdolata.simpleorderservice.domain.model.Order;
import pl.wdolata.simpleorderservice.domain.model.OrderItem;
import pl.wdolata.simpleorderservice.domain.model.valueobject.CouponCode;
import pl.wdolata.simpleorderservice.domain.model.valueobject.OrderId;
import pl.wdolata.simpleorderservice.domain.model.valueobject.UserId;

@Mapper
public interface OrderEntityMapper {

    @Mapping(target = "createdAt", source = "placedAt")
    OrderEntity toEntity(Order order);

    @Mapping(target = "placedAt", source = "createdAt")
    Order toDomain(OrderEntity entity);

    OrderItemData toData(OrderItem item);

    OrderItem toDomain(OrderItemData data);

    default OrderItemsData toData(List<OrderItem> items) {
        return new OrderItemsData(items.stream().map(this::toData).toList());
    }

    default List<OrderItem> toDomain(OrderItemsData data) {
        return data.items().stream().map(this::toDomain).toList();
    }

    default UUID toUuid(OrderId id) {
        return id.value();
    }

    default UUID toUuid(UserId id) {
        return id.value();
    }

    default UserId toUserId(UUID id) {
        return new UserId(id);
    }

    default OrderId toOrderId(UUID id) {
        return new OrderId(id);
    }

    default String toString(CouponCode code) {
        return code == null ? null : code.value();
    }

    default CouponCode toCouponCode(String code) {
        return code == null ? null : new CouponCode(code);
    }
}

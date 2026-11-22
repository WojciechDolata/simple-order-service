package pl.wdolata.simpleorderservice.adapters.kafka.publishers.orderevents.mapping;

import java.util.UUID;

import org.mapstruct.Mapper;
import pl.wdolata.simpleorderservice.adapters.kafka.publishers.orderevents.model.OrderPlacedMessage;
import pl.wdolata.simpleorderservice.domain.model.event.OrderPlacedEvent;
import pl.wdolata.simpleorderservice.domain.model.valueobject.CouponCode;
import pl.wdolata.simpleorderservice.domain.model.valueobject.OrderId;
import pl.wdolata.simpleorderservice.domain.model.valueobject.UserId;

@Mapper
public interface OrderPlacedMessageMapper {

    OrderPlacedMessage toMessage(OrderPlacedEvent event);

    default UUID toUuid(OrderId id) {
        return id.value();
    }

    default UUID toUuid(UserId id) {
        return id.value();
    }

    default String toString(CouponCode code) {
        return code == null ? null : code.value();
    }
}

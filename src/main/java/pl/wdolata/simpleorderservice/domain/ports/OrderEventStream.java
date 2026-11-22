package pl.wdolata.simpleorderservice.domain.ports;

import pl.wdolata.simpleorderservice.domain.model.event.OrderPlacedEvent;

public interface OrderEventStream {
    void publish(OrderPlacedEvent event);
}

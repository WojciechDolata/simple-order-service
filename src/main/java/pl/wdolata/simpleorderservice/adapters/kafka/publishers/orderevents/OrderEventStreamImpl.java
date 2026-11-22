package pl.wdolata.simpleorderservice.adapters.kafka.publishers.orderevents;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import pl.wdolata.simpleorderservice.adapters.kafka.publishers.orderevents.mapping.OrderPlacedMessageMapper;
import pl.wdolata.simpleorderservice.domain.model.event.OrderPlacedEvent;
import pl.wdolata.simpleorderservice.domain.ports.OrderEventStream;
import tools.jackson.databind.json.JsonMapper;

@Component
@RequiredArgsConstructor
public class OrderEventStreamImpl implements OrderEventStream {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final OrderPlacedMessageMapper mapper;
    private final JsonMapper jsonMapper;
    private final OrderEventsProperties properties;

    @Override
    public void publish(OrderPlacedEvent event) {
        var message = mapper.toMessage(event);
        kafkaTemplate.send(
            properties.orderEvents(),
            message.orderId().toString(),
            jsonMapper.writeValueAsString(message)
        );
    }
}

package pl.wdolata.simpleorderservice.adapters.kafka.publishers.orderevents;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("kafka.topics")
public record OrderEventsProperties(String orderEvents) {
}

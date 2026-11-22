package pl.wdolata.simpleorderservice.fixtures

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import pl.wdolata.simpleorderservice.support.BaseKafkaListenerFixture

@Component
class OrderEventsFixture extends BaseKafkaListenerFixture {

    @Value('${kafka.topics.order-events}')
    private String topic

    @Override
    protected String getTopic() {
        return topic
    }

}

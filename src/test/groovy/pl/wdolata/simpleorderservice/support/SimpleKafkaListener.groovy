package pl.wdolata.simpleorderservice.support

import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.ExecutionException

import net.javacrumbs.jsonunit.core.Configuration
import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.GroupState
import org.apache.kafka.common.errors.GroupIdNotFoundException
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.kafka.listener.MessageListener

/**
 * A real consumer on a real topic that records everything it sees, so that tests can
 * assert on what the service actually produced (key, headers, payload, how many times).
 */
class SimpleKafkaListener {

    private final List<ConsumerRecord<String, String>> messages = new CopyOnWriteArrayList<>()
    private final ConcurrentMessageListenerContainer<String, String> container

    SimpleKafkaListener(String bootstrapServers, String topic) {
        def groupId = UUID.randomUUID().toString()
        def props = [
            (ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG)       : bootstrapServers,
            (ConsumerConfig.GROUP_ID_CONFIG)                : groupId,
            (ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG)  : StringDeserializer,
            (ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG): StringDeserializer,
            (ConsumerConfig.AUTO_OFFSET_RESET_CONFIG)       : 'latest',
            (ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG)       : 10,
        ]
        container = new ConcurrentMessageListenerContainer<>(
            new DefaultKafkaConsumerFactory<String, String>(props), new ContainerProperties(topic))
        container.setupMessageListener((MessageListener<String, String>) { record -> messages.add(record) })
        container.start()
        try {
            waitUntilGroupIsStable(bootstrapServers, groupId)
        } catch (Throwable t) {
            container.destroy()
            throw t
        }
    }

    // With "latest" the consumer only sees messages sent after it joined, so we must wait for the join.
    private static void waitUntilGroupIsStable(String bootstrapServers, String groupId) {
        try (def admin = AdminClient.create([(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG): bootstrapServers])) {
            WaitUtils.waitUntil(30_000, 50) {
                assert groupState(admin, groupId) == GroupState.STABLE
            }
        }
    }

    private static GroupState groupState(AdminClient admin, String groupId) {
        try {
            return admin.describeConsumerGroups([groupId]).all().get().get(groupId).groupState()
        } catch (ExecutionException e) {
            if (e.cause instanceof GroupIdNotFoundException) {
                return GroupState.UNKNOWN
            }
            throw e
        }
    }

    int messageCount() {
        return messages.size()
    }

    void cleanup() {
        messages.clear()
    }

    ValidationResult validate(String expectedJson, String expectedKey, Map<String, String> expectedHeaders,
                              long expectedTimes, Configuration configuration) {
        return MessageValidator.validate(messages, expectedJson, expectedKey, expectedHeaders, expectedTimes,
            configuration)
    }

}

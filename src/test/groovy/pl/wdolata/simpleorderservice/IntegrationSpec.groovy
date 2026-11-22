package pl.wdolata.simpleorderservice

import java.time.Clock
import java.time.Instant

import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.kafka.KafkaContainer
import org.testcontainers.postgresql.PostgreSQLContainer
import pl.wdolata.simpleorderservice.domain.OrderIdGenerator
import pl.wdolata.simpleorderservice.domain.model.valueobject.OrderId
import pl.wdolata.simpleorderservice.fixtures.CouponServiceFixture
import pl.wdolata.simpleorderservice.fixtures.OrderApiFixture
import pl.wdolata.simpleorderservice.fixtures.OrderEventsFixture
import pl.wdolata.simpleorderservice.fixtures.OrdersFixture
import pl.wdolata.simpleorderservice.support.MockClock
import pl.wdolata.simpleorderservice.support.MockServer
import spock.lang.Specification

/**
 * Black-box base: real Postgres and Kafka (Testcontainers, started once per JVM),
 * a real HTTP server standing in for the coupon service, and a frozen clock and order id.
 * Everything else is the real application.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles('integration')
@ContextConfiguration
class IntegrationSpec extends Specification {

    static final Instant NOW = Instant.parse('2026-10-21T10:00:00Z')
    static final UUID USER_ID = UUID.fromString('7a1f4c2e-9b3d-4e58-a6c0-5d2e8f1b3a94')
    static final UUID ORDER_ID = UUID.fromString('0b1c2d3e-4f50-4a61-8b72-93a4b5c6d7e8')

    private static final PostgreSQLContainer POSTGRES = new PostgreSQLContainer('postgres:17-alpine')
    private static final KafkaContainer KAFKA = new KafkaContainer('apache/kafka-native:3.9.1')
    private static final MockServer COUPON_SERVICE

    static {
        POSTGRES.start()
        KAFKA.start()
        COUPON_SERVICE = new MockServer()
    }

    @DynamicPropertySource
    static void updateSpringProperties(DynamicPropertyRegistry registry) {
        registry.add('spring.datasource.url', POSTGRES::getJdbcUrl)
        registry.add('spring.datasource.username', POSTGRES::getUsername)
        registry.add('spring.datasource.password', POSTGRES::getPassword)
        registry.add('spring.kafka.bootstrap-servers', KAFKA::getBootstrapServers)
        registry.add('coupon-service.url', COUPON_SERVICE::baseUrl)
    }

    @SpringBean
    OrderIdGenerator orderIdGenerator = Stub {
        generate() >> new OrderId(ORDER_ID)
    }

    @Autowired
    OrderApiFixture api

    @Autowired
    OrdersFixture orders

    @Autowired
    CouponServiceFixture couponService

    @Autowired
    OrderEventsFixture orderEvents

    def setup() {
        orderEvents.setup()
    }

    def cleanup() {
        orders.flush()
        couponService.reset()
        orderEvents.cleanup()
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        @Primary
        Clock mockClock() {
            return new MockClock(NOW)
        }

        @Bean
        MockServer couponMockServer() {
            return COUPON_SERVICE
        }

    }

}

package pl.wdolata.simpleorderservice.config;

import java.time.Clock;
import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.wdolata.simpleorderservice.domain.OrderIdGenerator;
import pl.wdolata.simpleorderservice.domain.model.valueobject.OrderId;

@Configuration
public class AppConfig {

    @Bean
    Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    OrderIdGenerator orderIdGenerator() {
        return () -> new OrderId(UUID.randomUUID());
    }
}

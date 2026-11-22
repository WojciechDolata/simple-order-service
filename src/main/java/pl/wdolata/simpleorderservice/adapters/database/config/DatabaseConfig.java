package pl.wdolata.simpleorderservice.adapters.database.config;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import pl.wdolata.simpleorderservice.adapters.database.orders.converters.OrderItemsReadingConverter;
import pl.wdolata.simpleorderservice.adapters.database.orders.converters.OrderItemsWritingConverter;
import tools.jackson.databind.json.JsonMapper;

@Configuration
@RequiredArgsConstructor
public class DatabaseConfig extends AbstractJdbcConfiguration {
    private final JsonMapper jsonMapper;

    @Override
    protected @NonNull List<?> userConverters() {
        return List.of(
            new OrderItemsReadingConverter(jsonMapper),
            new OrderItemsWritingConverter(jsonMapper)
        );
    }
}

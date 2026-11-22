package pl.wdolata.simpleorderservice.adapters.database.orders.converters;

import java.sql.SQLException;

import lombok.RequiredArgsConstructor;
import org.postgresql.util.PGobject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import pl.wdolata.simpleorderservice.adapters.database.orders.model.OrderItemsData;
import tools.jackson.databind.json.JsonMapper;

@WritingConverter
@RequiredArgsConstructor
public class OrderItemsWritingConverter implements Converter<OrderItemsData, PGobject> {
    private final JsonMapper jsonMapper;

    @Override
    public PGobject convert(OrderItemsData source) {
        try {
            var json = new PGobject();
            json.setType("jsonb");
            json.setValue(jsonMapper.writeValueAsString(source));
            return json;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}

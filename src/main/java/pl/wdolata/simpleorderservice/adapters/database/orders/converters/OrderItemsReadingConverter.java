package pl.wdolata.simpleorderservice.adapters.database.orders.converters;

import lombok.RequiredArgsConstructor;
import org.postgresql.util.PGobject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import pl.wdolata.simpleorderservice.adapters.database.orders.model.OrderItemsData;
import tools.jackson.databind.json.JsonMapper;

@ReadingConverter
@RequiredArgsConstructor
public class OrderItemsReadingConverter implements Converter<PGobject, OrderItemsData> {
    private final JsonMapper jsonMapper;

    @Override
    public OrderItemsData convert(PGobject source) {
        return jsonMapper.readValue(source.getValue(), OrderItemsData.class);
    }
}

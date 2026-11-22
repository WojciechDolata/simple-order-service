package pl.wdolata.simpleorderservice.adapters.database.orders.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public record OrderItemsData(@JsonValue List<OrderItemData> items) {

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public OrderItemsData {
    }
}

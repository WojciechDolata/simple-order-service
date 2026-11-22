package pl.wdolata.simpleorderservice.adapters.database.orders.model;

import java.math.BigDecimal;

public record OrderItemData(String sku, int quantity, BigDecimal unitPrice) {
}

package pl.wdolata.simpleorderservice.domain.ports;

import pl.wdolata.simpleorderservice.domain.model.Order;

public interface OrderStore {
    Order save(Order order);
}

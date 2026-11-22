package pl.wdolata.simpleorderservice.domain;

import pl.wdolata.simpleorderservice.domain.model.valueobject.OrderId;

@FunctionalInterface
public interface OrderIdGenerator {
    OrderId generate();
}

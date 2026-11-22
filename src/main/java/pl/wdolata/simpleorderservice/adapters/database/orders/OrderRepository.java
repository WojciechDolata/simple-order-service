package pl.wdolata.simpleorderservice.adapters.database.orders;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<OrderEntity, UUID> {
}

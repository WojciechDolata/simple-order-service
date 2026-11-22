package pl.wdolata.simpleorderservice.adapters.database.orders;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.wdolata.simpleorderservice.adapters.database.orders.mapping.OrderEntityMapper;
import pl.wdolata.simpleorderservice.domain.model.Order;
import pl.wdolata.simpleorderservice.domain.ports.OrderStore;

@Service
@RequiredArgsConstructor
public class OrderStoreImpl implements OrderStore {
    private final OrderRepository orderRepository;
    private final OrderEntityMapper mapper;

    @Override
    public Order save(Order order) {
        return mapper.toDomain(orderRepository.save(mapper.toEntity(order)));
    }
}

package pl.wdolata.simpleorderservice.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.wdolata.simpleorderservice.api.mapping.OrderApiMapper;
import pl.wdolata.simpleorderservice.api.model.OrderResponse;
import pl.wdolata.simpleorderservice.api.model.PlaceOrderRequest;
import pl.wdolata.simpleorderservice.domain.PlaceOrderService;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final PlaceOrderService placeOrderService;
    private final OrderApiMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse placeOrder(@Valid @RequestBody PlaceOrderRequest request) {
        return mapper.toResponse(placeOrderService.placeOrder(mapper.toCommand(request)));
    }
}

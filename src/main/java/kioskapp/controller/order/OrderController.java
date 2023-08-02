package kioskapp.controller.order;

import kioskapp.service.order.OrderService;
import kioskapp.service.order.dto.OrderCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/api/v1/orders/")
    public void postOrder(@RequestBody OrderCreateRequest orderCreateRequest) {
        // TODO: Implement
    }
}

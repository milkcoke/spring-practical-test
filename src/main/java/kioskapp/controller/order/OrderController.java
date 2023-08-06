package kioskapp.controller.order;

import jakarta.validation.Valid;
import kioskapp.common.ApiResponse;
import kioskapp.service.order.OrderService;
import kioskapp.service.order.dto.OrderCreateRequest;
import kioskapp.service.order.dto.OrderCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/api/v1/orders")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ApiResponse<OrderCreateResponse> postOrder(@Valid @RequestBody OrderCreateRequest orderCreateRequest) {
        return ApiResponse.ok(orderService.createOrder(orderCreateRequest));
    }
}

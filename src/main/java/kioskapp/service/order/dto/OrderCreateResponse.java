package kioskapp.service.order.dto;

import kioskapp.domain.order.Order;
import kioskapp.service.product.dto.ProductResponse;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderCreateResponse {
    private Long id;

    private int totalPrice;

    private LocalDateTime registeredDateTime;

    @Builder.Default
    private List<ProductResponse> productResponses = new ArrayList<>();

    @Builder
    private OrderCreateResponse(Long id, int totalPrice, LocalDateTime registeredDateTime, List<ProductResponse> productResponses) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.registeredDateTime = registeredDateTime;
        this.productResponses = productResponses;
    }

    // Order 를 인자로 받는 아이디어는
    // Req DTO -> Domain entity class -> Res DTO 에서 왔다.
    // Service Layer code 가 factory method 로 간결해진다.
    public static OrderCreateResponse of(Order order) {
        return OrderCreateResponse.builder()
                .id(order.getId())
                .totalPrice(order.getTotalPrice())
                .registeredDateTime(order.getCreateDateTime())
                .productResponses(order.getOrderProducts().stream()
                        .map(orderProduct -> ProductResponse.of(orderProduct.getProduct()))
                        .collect(Collectors.toList())
                )
                .build();
    }
}

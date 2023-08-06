package kioskapp.controller.order.dto;

import jakarta.validation.constraints.NotEmpty;
import kioskapp.service.order.dto.OrderCreateServiceRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderCreateRequest {
    // 오로지 상품 번호로만 주문한다고 가정.
    @NotEmpty(message = "product number is required")
    private List<String> productNumbers;

    @Builder
    private OrderCreateRequest(List<String> productNumbers) {
        this.productNumbers = productNumbers;
    }

    public OrderCreateServiceRequest toServiceRequest() {
        return OrderCreateServiceRequest.builder()
                .productNumbers(this.productNumbers)
                .build();
    }
}

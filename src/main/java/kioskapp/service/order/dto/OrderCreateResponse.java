package kioskapp.service.order.dto;

import kioskapp.service.product.dto.ProductResponse;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class OrderCreateResponse {
    private Long id;

    private int totalPrice;

    private List<ProductResponse> productResponses = new ArrayList<>();
}

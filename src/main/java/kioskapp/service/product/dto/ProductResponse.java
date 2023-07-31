package kioskapp.service.product.dto;

import kioskapp.domain.product.Product;
import kioskapp.domain.product.ProductSellingStatus;
import kioskapp.domain.product.ProductType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductResponse {
    private Long id;
    private String name;
    private int price;
    private String productNumber;
    private ProductType type;
    private ProductSellingStatus sellingStatus;

    @Builder
    private ProductResponse(Long id, String name, int price, String productNumber, ProductType type, ProductSellingStatus sellingStatus) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.productNumber = productNumber;
        this.type = type;
        this.sellingStatus = sellingStatus;
    }

    public static ProductResponse of(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .productNumber(product.getProductNumber())
                .type(product.getType())
                .sellingStatus(product.getSellingStatus())
                .build();
    }
}

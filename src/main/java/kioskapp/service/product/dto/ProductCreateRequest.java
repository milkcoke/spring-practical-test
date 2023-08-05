package kioskapp.service.product.dto;

import kioskapp.domain.product.Product;
import kioskapp.domain.product.ProductSellingStatus;
import kioskapp.domain.product.ProductType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductCreateRequest {
  private String name;
  private ProductType type;
  private ProductSellingStatus sellingStatus;
  private int price;

  @Builder
  private ProductCreateRequest(String name, ProductType type, ProductSellingStatus sellingStatus, int price) {
    this.name = name;
    this.type = type;
    this.sellingStatus = sellingStatus;
    this.price = price;
  }

  public Product toEntity(String nextProductNumber) {
    return Product.builder()
        .name(this.getName())
        .type(this.getType())
        .productNumber(nextProductNumber)
        .sellingStatus(this.getSellingStatus())
        .price(this.getPrice())
        .build();
  }
}

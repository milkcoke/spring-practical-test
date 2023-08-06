package kioskapp.service.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import kioskapp.domain.product.Product;
import kioskapp.domain.product.ProductSellingStatus;
import kioskapp.domain.product.ProductType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor // ObjectMapper 가 역직렬화시 기본 생성자 사용.
public class ProductCreateRequest {
  // 상품 이름 20자 제한이 있으면 여기서 막는게 나을까?
  // Controller 에서 팅겨 내는게 맞나?
  // NotBlank Not null 은 General 한 validation 이지만
  // 길이 제한 값 제한은 도메인 정책에 가깝기 때문에 Presentation Layer 책임이라 보기 어렵다.
  // 성격에 따라 어느 Layer 에서 검증할지 Validation 책임 분리를 고민해봐야함.
  @NotBlank(message = "product name is required")
  private String name;

  @NotNull(message = "product type is required")
  private ProductType type;

  @NotNull(message = "product selling status is required")
  private ProductSellingStatus sellingStatus;

  @PositiveOrZero(message = "price should be greater than or equal to zero")
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

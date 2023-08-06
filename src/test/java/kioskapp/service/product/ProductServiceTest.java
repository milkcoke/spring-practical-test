package kioskapp.service.product;

import kioskapp.domain.product.Product;
import kioskapp.domain.product.ProductSellingStatus;
import kioskapp.domain.product.ProductType;
import kioskapp.domain.stock.Stock;
import kioskapp.respository.product.ProductRepository;
import kioskapp.controller.product.dto.ProductCreateRequest;
import kioskapp.service.product.dto.ProductResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.*;

@ActiveProfiles("test")
@SpringBootTest
class ProductServiceTest {

  @Autowired
  private ProductService productService;
  @Autowired
  private ProductRepository productRepository;

  @AfterEach()
  void tearDown() {
    this.productRepository.deleteAllInBatch();
  }

  Product appleBottle = Product.builder()
      .name("아침에 사과")
      .productNumber("001")
      .type(ProductType.BOTTLE)
      .price(3000)
      .sellingStatus(ProductSellingStatus.SELLING)
      .stock(new Stock(5))
      .build();

  @Test
  @DisplayName("첫 상품 등록시 상품번호가 001로 등록된다.")
  public void createFirstProduct() {
    // given
    ProductCreateRequest appleCreateRequest = ProductCreateRequest.builder()
        .name(appleBottle.getName())
        .type(appleBottle.getType())
        .sellingStatus(appleBottle.getSellingStatus())
        .price(appleBottle.getPrice())
        .build();

    // when
    ProductResponse appleCreateResponse = productService.createProduct(appleCreateRequest.toServiceRequest());

    // then
    assertThat(appleCreateResponse)
        .extracting("name", "productNumber")
        .containsOnly("아침에 사과", "001");
  }

  @Test
  @DisplayName("신규 상품 등록시 마지막 상품 번호에서 1 증가한 값으로 등록된다.")
  void createProduct() {
    // given
    productRepository.save(appleBottle);
    Product pear = Product.builder()
                                          .name("갈아만든 배")
                                          .type(ProductType.BOTTLE)
                                          .price(2500)
                                          .sellingStatus(ProductSellingStatus.SELLING)
                                          .stock(new Stock(5))
                                          .build();

    ProductCreateRequest pearCreateRequest = ProductCreateRequest.builder()
        .name(pear.getName())
        .type(pear.getType())
        .sellingStatus(pear.getSellingStatus())
        .price(pear.getPrice())
        .build();

    // when
    ProductResponse pearCreateResponse = productService.createProduct(pearCreateRequest.toServiceRequest());

    // then
    assertThat(pearCreateResponse)
        .extracting("name", "productNumber", "type", "price", "sellingStatus")
        .containsOnly("갈아만든 배", "002", ProductType.BOTTLE, 2500, ProductSellingStatus.SELLING);

    var allProducts = productRepository.findAll();
    assertThat(allProducts).hasSize(2)
        .extracting("name", "productNumber")
        .containsExactly(
            tuple("아침에 사과", "001"),
            tuple("갈아만든 배", "002")
        );
  }
}
package kioskapp.service.product;

import kioskapp.IntegrationTestSupport;
import kioskapp.domain.product.Product;
import kioskapp.domain.product.ProductSellingStatus;
import kioskapp.domain.product.ProductType;
import kioskapp.domain.stock.Stock;
import kioskapp.respository.orderproduct.OrderProductRepository;
import kioskapp.respository.product.ProductRepository;
import kioskapp.controller.product.dto.ProductCreateRequest;
import kioskapp.service.product.dto.ProductResponse;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.*;

class ProductServiceTest extends IntegrationTestSupport {

  @Autowired
  private ProductService productService;
  @Autowired
  private ProductRepository productRepository;

  @DisplayName("상품 등록 시나리오")
  @TestFactory
  Collection<DynamicTest> dynamicTest() {
    // First resource
    ProductCreateRequest appleCreateRequest = ProductCreateRequest.builder()
        .name("아침에 사과")
        .type(ProductType.BOTTLE)
        .sellingStatus(ProductSellingStatus.SELLING)
        .price(3000)
        .build();

    return List.of(
        DynamicTest.dynamicTest("첫 상품 등록시 상품번호 001로 등록", ()-> {
          // when
          ProductResponse appleCreateResponse = productService.createProduct(appleCreateRequest.toServiceRequest());

          // then
          assertThat(appleCreateResponse)
              .extracting("name", "productNumber")
              .containsOnly("아침에 사과", "001");
        }),

        DynamicTest.dynamicTest("두번째 상품 등록시 001에서 1 증가한 002로 등록", ()->{
          ProductCreateRequest pearCreateRequest = ProductCreateRequest.builder()
              .name("갈아만든 배")
              .type(ProductType.BOTTLE)
              .sellingStatus(ProductSellingStatus.SELLING)
              .price(2500)
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
        })
    );
  }
}
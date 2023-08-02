package kioskapp.service.order;

import kioskapp.domain.product.Product;
import kioskapp.domain.product.ProductType;
import kioskapp.respository.product.ProductRepository;
import kioskapp.service.order.dto.OrderCreateRequest;
import kioskapp.service.order.dto.OrderCreateResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static kioskapp.domain.product.ProductSellingStatus.*;
import static kioskapp.domain.product.ProductType.BAKERY;
import static kioskapp.domain.product.ProductType.HANDMADE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@ActiveProfiles("test")
@SpringBootTest
class OrderServiceTest {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductRepository productRepository;

    // given 에 필요한 데이터만 주기 위해 product 생성 helper 메소드
    // 테스트에 필요한 메소드 생성
    private Product createProduct(ProductType productType, String productNumber, String name, int price) {
        return Product.builder()
                .type(productType)
                .productNumber(productNumber)
                .name(name)
                .price(price)
                .sellingStatus(SELLING)
                .build();
    }

    @Test
    @DisplayName("주문 번호 리스트를 받아 주문 생성")
    public void createOrder() {
      // given
      Product americano = createProduct(HANDMADE, "001", "아메리카노", 4000);
      Product cafeLatte = createProduct(HANDMADE, "002", "카페라떼", 4500);
      Product pineappleBread = createProduct(BAKERY, "003", "소보로빵", 1500);
      productRepository.saveAll(List.of(americano, cafeLatte, pineappleBread));
      OrderCreateRequest orderCreateRequest = OrderCreateRequest.builder()
              .productNumbers(List.of("001, 002"))
              .build();

      // when
      OrderCreateResponse orderCreateResponse = orderService.createOrder(orderCreateRequest);

      // then
      assertThat(orderCreateResponse.getId()).isNotNull();
      assertThat(orderCreateResponse.getProductResponses()).hasSize(2)
              .extracting("productNumber", "name", "price")
              .containsExactlyInAnyOrder(
                      tuple("001", "아메리카노", 4000),
                      tuple("002", "카페라떼", 4500)
              );
    }
}
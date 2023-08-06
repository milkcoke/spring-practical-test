package kioskapp.service.order;

import kioskapp.domain.product.Product;
import kioskapp.domain.product.ProductType;
import kioskapp.domain.stock.Stock;
import kioskapp.respository.order.OrderRepository;
import kioskapp.respository.orderproduct.OrderProductRepository;
import kioskapp.respository.product.ProductRepository;
import kioskapp.respository.stock.StockRepository;
import kioskapp.controller.order.dto.OrderCreateRequest;
import kioskapp.service.order.dto.OrderCreateResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static kioskapp.domain.product.ProductSellingStatus.SELLING;
import static kioskapp.domain.product.ProductType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;

@ActiveProfiles("test")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderServiceTest {
  @Autowired
  private OrderService orderService;
  @Autowired
  private ProductRepository productRepository;
  @Autowired
  private OrderProductRepository orderProductRepository;
  @Autowired
  private OrderRepository orderRepository;
  @Autowired
  private StockRepository stockRepository;

  @AfterAll
  void cleanUp() {
    this.orderProductRepository.deleteAllInBatch();
    this.productRepository.deleteAllInBatch();
    this.orderRepository.deleteAllInBatch();
    this.stockRepository.deleteAllInBatch();
  }
    @Test
    @DisplayName("주문 번호 리스트를 받아 주문 생성")
    public void createOrder() {
      // given
      Product americano = createProduct(HANDMADE, "001", "아메리카노", 4000, null);
      Product cafeLatte = createProduct(HANDMADE, "002", "카페라떼", 4500, null);
      Product pineappleBread = createProduct(BAKERY, "003", "소보로빵", 1500, new Stock(10));

      productRepository.saveAll(List.of(americano, cafeLatte, pineappleBread));
      OrderCreateRequest orderCreateRequest = OrderCreateRequest.builder()
              .productNumbers(List.of("001", "002"))
              .build();

      // when
      OrderCreateResponse orderCreateResponse = orderService.createOrder(orderCreateRequest.toServiceRequest());

      // then
      assertThat(orderCreateResponse.getId()).isNotNull();
      assertThat(orderCreateResponse.getProductResponses()).hasSize(2)
              .extracting("productNumber", "name", "price")
              .containsExactlyInAnyOrder(
                      tuple("001", "아메리카노", 4000),
                      tuple("002", "카페라떼", 4500)
              );
    }

    @Test
    @DisplayName("중복 상품 여러개 주문 가능")
    public void orderDuplicateProductNumbers() {
      // given
      Product coldBrew = createProduct(HANDMADE, "009", "콜드브루", 5500, null);
      productRepository.save(coldBrew);
      OrderCreateRequest orderRequest = OrderCreateRequest.builder()
                .productNumbers(List.of("009", "009"))
                .build();

      // when
      OrderCreateResponse orderCreateResponse = orderService.createOrder(orderRequest.toServiceRequest());
      // then
      assertThat(orderCreateResponse.getProductResponses()).hasSize(2)
              .extracting("name", "price")
              .containsExactlyInAnyOrder(
                      tuple("콜드브루", 5500),
                      tuple("콜드브루", 5500)
              );
    }

    // given 에 필요한 데이터만 주기 위해 product 생성 helper 메소드
    // 테스트에 필요한 메소드 생성
    private Product createProduct(ProductType productType, String productNumber, String name, int price, Stock stock) {
        return Product.builder()
                .type(productType)
                .productNumber(productNumber)
                .name(name)
                .price(price)
                .sellingStatus(SELLING)
                .stock(stock)
                .build();
    }

    @Test
    @DisplayName("병음료, 빵 등 재고가 남은 상품번호 리스트를 받아 주문하여 정상 재고 차감된다.")
    public void createOrderWithStock() {
        // given
        Product cafeMocha = createProduct(HANDMADE, "004", "카페모카", 4000, null);
        Product grapefruitSmoothie = createProduct(BOTTLE, "005", "자몽 스무디", 5500, new Stock(10));
        Product bread = createProduct(BAKERY, "006", "찰깨빵", 1500, new Stock(10));
        var products = List.of(cafeMocha, grapefruitSmoothie, bread);
        productRepository.saveAll(products);
        var productNumbers = List.of("004", "005","005","005","006", "006");
        var orderRequest = OrderCreateRequest.builder().productNumbers(productNumbers).build();

        // when
        orderService.createOrder(orderRequest.toServiceRequest());

        // then
        var updateCafeMocha = productRepository.findById(cafeMocha.getId());
        var updatedGrapeFruitSmoothie = productRepository.findById(grapefruitSmoothie.getId());
        var updatedBread = productRepository.findById(bread.getId());
        assertThat(updateCafeMocha.orElseThrow().getStock()).isNull();
        assertThat(updatedGrapeFruitSmoothie.orElseThrow().getStock().getQuantity()).isEqualTo(7);
        assertThat(updatedBread.orElseThrow().getStock().getQuantity()).isEqualTo(8);
    }

    @Test
    @DisplayName("재고가 0인 상품을 주문하면 주문이 취소된다.")
    public void createOrderWithNoStock() {
        // given
        Product morningApple = createProduct(BOTTLE, "007", "아침에 사과", 2500, new Stock(0));
        productRepository.save(morningApple);
        var productNumbers = List.of("007");
        var orderRequest = OrderCreateRequest.builder().productNumbers(productNumbers).build();

        // when // then
        assertThatThrownBy(() -> orderService.createOrder(orderRequest.toServiceRequest()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("재고가 부족한 상품을 주문했습니다.");
    }

    @Test
    @DisplayName("재고가 부족한 상품을 주문하면 주문이 취소된다.")
    public void createOrderWithNotEnoughStock() {
      // given
      Product garlicBread = createProduct(BOTTLE, "008", "마늘빵", 3000, new Stock(1));
      productRepository.save(garlicBread);
      var productNumbers = List.of("008", "008");
      var orderRequest = OrderCreateRequest.builder().productNumbers(productNumbers).build();

      // when // then
      assertThatThrownBy(() -> orderService.createOrder(orderRequest.toServiceRequest()))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("재고가 부족한 상품을 주문했습니다.");
    }
}

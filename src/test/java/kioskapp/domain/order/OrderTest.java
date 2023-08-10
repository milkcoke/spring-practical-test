package kioskapp.domain.order;

import kioskapp.config.JpaAuditingConfig;
import kioskapp.domain.orderproduct.OrderProduct;
import kioskapp.domain.product.Product;
import kioskapp.domain.product.ProductType;
import kioskapp.domain.stock.Stock;
import kioskapp.respository.order.OrderRepository;
import kioskapp.respository.product.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest(includeFilters = @ComponentScan.Filter(
    type = FilterType.ASSIGNABLE_TYPE,
    classes = JpaAuditingConfig.class
))
class OrderTest {
    @Autowired
    private OrderRepository orderRepository;
    Product americano = Product.builder()
            .name("아메리카노")
            .productNumber("001")
            .price(4000)
            .type(ProductType.HANDMADE)
            .build();

    Product latte = Product.builder()
            .name("카페라떼")
            .productNumber("002")
            .price(4500)
            .type(ProductType.HANDMADE)
            .build();

    Product pineappleBread = Product.builder()
            .name("소보로빵")
            .productNumber("003")
            .price(1500)
            .type(ProductType.BAKERY)
            .stock(new Stock(5))
            .build();
    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("주문 시점에 주문 총 금액 표기")
    void calculateTotalPrice() {

        // given
        Order order = new Order();
        var products = List.of(americano, latte, pineappleBread);
         products.stream()
                .map(OrderProduct::new)
                .forEach(orderProduct -> orderProduct.updateOrder(order));

        // when
        orderRepository.save(order);

        // then
        assertThat(order.getTotalPrice()).isEqualTo(10_000);
    }

    @Test
    @DisplayName("최초 주문시 상태는 INIT 이다.")
    public void initStatus() {
        // given
        Order order = new Order();

        // when // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.INIT);
    }

    @Test
    public void checkCreatedTime() {
        // given
        Order order = new Order();
        var products = List.of(americano, latte, pineappleBread);
        products.stream()
                .map(OrderProduct::new)
                .forEach(orderProduct -> orderProduct.updateOrder(order));

        this.productRepository.saveAll(products);
        // when
        this.orderRepository.save(order);

        // then
        assertThat(order.getCreateDateTime())
                .isNotNull()
                .isBeforeOrEqualTo(LocalDateTime.now());
    }
}

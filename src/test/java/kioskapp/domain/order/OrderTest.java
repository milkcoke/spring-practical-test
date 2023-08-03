package kioskapp.domain.order;

import kioskapp.domain.orderproduct.OrderProduct;
import kioskapp.domain.product.Product;
import kioskapp.respository.order.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OrderTest {
    @Autowired
    private OrderRepository orderRepository;
    Product americano = Product.builder()
            .name("Americano")
            .price(4000)
            .build();

    Product latte = Product.builder()
            .name("latte")
            .price(4500)
            .build();

    Product pineappleBread = Product.builder()
            .name("소보로빵")
            .price(1500)
            .build();
    @Test
    @DisplayName("주문 시점에 주문 총 금액 표기")
    void calculateTotalPrice() {
        // given
        var products = List.of(americano, latte, pineappleBread);
        var orderProducts = products.stream()
                .map(OrderProduct::new)
                .toList();

        // when
        Order order = new Order(orderProducts);

        // then
        assertThat(order.getTotalPrice()).isEqualTo(10_000);
    }

    @Test
    @DisplayName("최초 주문시 상태는 INIT 이다.")
    public void initStatus() {
        // given
        var products = List.of(americano, latte, pineappleBread);
        var orderProducts = products.stream()
                .map(OrderProduct::new)
                .toList();

        // when
        Order order = new Order(orderProducts);
        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.INIT);
    }

    @Test
    public void checkCreatedTime() {
        // given
        var products = List.of(americano, latte, pineappleBread);
        var orderProducts = products.stream()
                .map(OrderProduct::new)
                .toList();

        Order order = new Order(orderProducts);
        // when
        this.orderRepository.save(order);
        // then
        assertThat(order.getCreateDateTime())
                .isNotNull()
                .isBeforeOrEqualTo(LocalDateTime.now());
    }
}

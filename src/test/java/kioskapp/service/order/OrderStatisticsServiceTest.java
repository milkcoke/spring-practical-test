package kioskapp.service.order;

import kioskapp.IntegrationTestSupport;
import kioskapp.domain.history.mail.MailSendHistory;
import kioskapp.domain.order.Order;
import kioskapp.domain.order.OrderStatus;
import kioskapp.domain.orderproduct.OrderProduct;
import kioskapp.domain.product.Product;
import kioskapp.domain.product.ProductType;
import kioskapp.domain.stock.Stock;
import kioskapp.respository.history.mail.MailSendHistoryRepository;
import kioskapp.respository.order.OrderRepository;
import kioskapp.respository.product.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderStatisticsServiceTest extends IntegrationTestSupport {

  @Autowired
  private OrderStatisticsService orderStatisticsService;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private MailSendHistoryRepository mailSendHistoryRepository;

  @Test
  @DisplayName("결제 완료 주문의 일일 매출 통계 메일을 전송한다.")
  void sendDayTotalSalesToMail() {
    // given
    Product cafeMocha = Product.builder()
        .name("카페모카")
        .productNumber("100")
        .price(4000)
        .type(ProductType.HANDMADE)
        .build();

    Product latte = Product.builder()
        .name("콜드브루")
        .productNumber("101")
        .price(4500)
        .type(ProductType.HANDMADE)
        .build();

    Product bread = Product.builder()
        .name("찰깨빵")
        .productNumber("102")
        .price(1500)
        .type(ProductType.BAKERY)
        .stock(new Stock(5))
        .build();

    var americanoOrderProduct = new OrderProduct(cafeMocha);
    var latteOrderProduct = new OrderProduct(latte);
    var breadOrderProduct = new OrderProduct(bread);

    productRepository.saveAll(List.of(cafeMocha, latte, bread));
    Order order = new Order();
    List.of(americanoOrderProduct, latteOrderProduct, breadOrderProduct).forEach(
        orderProduct -> orderProduct.updateOrder(order)
    );
    order.updateOrderStatus(OrderStatus.PAYMENT_COMPLETED);

    orderRepository.save(order);

    // when
    orderStatisticsService.sendDayTotalSalesToMail(LocalDate.now(), "test@test.com");

    // then
    List<MailSendHistory> sendHistories = mailSendHistoryRepository.findAll();
    assertThat(sendHistories).hasSize(1)
        .extracting("content")
        .containsOnly(String.format("총 매출 합계는 %s 입니다.", 10_000));
  }
}
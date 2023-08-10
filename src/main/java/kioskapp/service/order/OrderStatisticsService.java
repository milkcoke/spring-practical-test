package kioskapp.service.order;

import kioskapp.domain.order.Order;
import kioskapp.domain.order.OrderStatus;
import kioskapp.respository.order.OrderRepository;
import kioskapp.service.mail.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderStatisticsService {
    private final OrderRepository orderRepository;
    private final MailService mailService;

    @Transactional
    public void sendDayTotalSalesToMail(LocalDate orderDate, String email) {
        List<Order> orders = orderRepository.findOrdersBy(
                orderDate.atStartOfDay(),
                orderDate.plusDays(1).atStartOfDay(),
                OrderStatus.PAYMENT_COMPLETED
        );

        int dayTotalSales = orders.stream()
                .mapToInt(Order::getTotalPrice)
                .sum();

        try {
            mailService.sendMail(
                    "no-reply@cafekiosk.com",
                    email,
                    String.format("%s 일일 매출", orderDate),
                    String.format("총 매출 합계는 %s 입니다.", dayTotalSales)
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("일일 매출 통계 이메일 전송에 실패했습니다.");
        }

    }
}

package kioskapp.service.order;

import kioskapp.domain.order.Order;
import kioskapp.domain.product.Product;
import kioskapp.respository.order.OrderRepository;
import kioskapp.respository.product.ProductRepository;
import kioskapp.service.order.dto.OrderCreateRequest;
import kioskapp.service.order.dto.OrderCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public OrderCreateResponse createOrder(OrderCreateRequest orderCreateRequest) {
        List<String> productNumbers = orderCreateRequest.getProductNumbers();
        List<Product> products = this.productRepository.findAllByProductNumberIn(productNumbers);
        Order order = new Order(products);
        this.orderRepository.save(order);

        return OrderCreateResponse.of(order);
    }
}

package kioskapp.service.order;

import kioskapp.domain.product.Product;
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
    public OrderCreateResponse createOrder(OrderCreateRequest orderCreateRequest) {
        List<String> productNumbers = orderCreateRequest.getProductNumbers();
        List<Product> products = this.productRepository.findAllByProductNumberIn(productNumbers);
        // TODO: Implement Order service with order and products
    }
}

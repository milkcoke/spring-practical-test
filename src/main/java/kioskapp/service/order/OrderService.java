package kioskapp.service.order;

import kioskapp.domain.order.Order;
import kioskapp.domain.orderproduct.OrderProduct;
import kioskapp.domain.product.Product;
import kioskapp.respository.order.OrderRepository;
import kioskapp.respository.orderproduct.OrderProductRepository;
import kioskapp.respository.product.ProductRepository;
import kioskapp.service.order.dto.OrderCreateRequest;
import kioskapp.service.order.dto.OrderCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;
    private final OrderRepository orderRepository;

    public OrderCreateResponse createOrder(OrderCreateRequest orderCreateRequest) {
        List<String> productNumbers = orderCreateRequest.getProductNumbers();
        List<Product> uniqueProducts = this.productRepository.findAllByProductNumberIn(productNumbers);
        List<Product> allProducts = getAllProductByProductNumbers(uniqueProducts, productNumbers);

        List<OrderProduct> orderProducts = allProducts.stream()
                .map(OrderProduct::new)
                .toList();

        Order order = new Order(orderProducts);
        this.orderRepository.save(order);

        // TODO
        //      Group By 주문번호 -> 상품별 재고 파악.
        //      재고 조회
        //      재고 차감 시도
        //      성공시 넘어가고 아니면 에러처리
        return OrderCreateResponse.of(order);
    }

    private List<Product> getAllProductByProductNumbers(List<Product> uniqueProducts, List<String> productNumbers) {
        // productNumbers 에 매핑된 product 를 모두 가져온다.
        Map<String, Product> productNumberMap = uniqueProducts.stream()
                        .collect(Collectors.toMap(Product::getProductNumber, product->product));

        return productNumbers.stream()
                .map(productNumberMap::get)
                .toList();
    }
}

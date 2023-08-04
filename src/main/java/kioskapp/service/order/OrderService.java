package kioskapp.service.order;

import kioskapp.domain.order.Order;
import kioskapp.domain.order.OrderStatus;
import kioskapp.domain.orderproduct.OrderProduct;
import kioskapp.domain.product.Product;
import kioskapp.domain.product.ProductType;
import kioskapp.respository.order.OrderRepository;
import kioskapp.respository.orderproduct.OrderProductRepository;
import kioskapp.respository.product.ProductRepository;
import kioskapp.service.order.dto.OrderCreateRequest;
import kioskapp.service.order.dto.OrderCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

        // (1) 재고관리 대상 상품ID : 현재 주문량  // findAllProductByProductNumbersIn 로 ID 구하고, collect.counting 으로 주문 개수 확인
        List<Product> productsHavingStockList = getProductsHavingStock(allProducts);

        Set<Product> productsHavingStockSet = new HashSet<>(productsHavingStockList);
        Map<String, Long> mapProductCount =productsHavingStockList.stream()
                        .map(Product::getProductNumber)
                        .collect(Collectors.groupingBy(productNumber->productNumber, Collectors.counting()));

        // (2) ID별 재고 조회
        for (var product : productsHavingStockSet) {
            // 무조건 map 에 있는 값만 남아있을 것.
            var productQuantity = mapProductCount.get(product.getProductNumber());
            var stock = product.getStock();
            if (!stock.hasEnoughQuantity(productQuantity.intValue())) {
                // (3 - A) 재고 없으면 예외하고 주문 상태 변경
                order.updateOrderStatus(OrderStatus.CANCELED);
                throw new IllegalArgumentException("재고가 부족한 상품을 주문했습니다.");
            }
        }

        // (3 - B) 재고 있으면 재고 차감
        for (Product product : productsHavingStockSet) {
            // productId 에 해당하는 product 를 불러올 수 가 없네 아 ..
            // 위에서 이미 재고 조회 했으니 검사 없이 차감만.
            var productQuantity = mapProductCount.get(product.getProductNumber());
            var stock = product.getStock();
            stock.deductQuantity(productQuantity.intValue());
        }

        this.productRepository.saveAll(productsHavingStockSet.stream().toList());

        // (4) 주문 처리
        return OrderCreateResponse.of(order);
    }

    private List<Product> getProductsHavingStock(List<Product> products) {
        return products.stream()
                .filter(product -> ProductType.hasStock(product.getType()))
                .toList();
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

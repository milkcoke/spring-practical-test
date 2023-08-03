package kioskapp.domain.order;

import jakarta.persistence.*;
import kioskapp.domain.BaseEntity;
import kioskapp.domain.orderproduct.OrderProduct;
import kioskapp.domain.product.Product;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private int totalPrice;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    public Order(List<OrderProduct> orderProducts) {
        this.orderStatus = OrderStatus.INIT;
        this.totalPrice = calculateTotalPrice(orderProducts);
        this.orderProducts = orderProducts;
        for (var orderProduct  : orderProducts) {
            orderProduct.updateOrder(this);
        }
    }

    private static int calculateTotalPrice(List<OrderProduct> orderProducts) {
        return orderProducts.stream()
                .map(OrderProduct::getProduct)
                .mapToInt(Product::getPrice)
                .sum();
    }
}

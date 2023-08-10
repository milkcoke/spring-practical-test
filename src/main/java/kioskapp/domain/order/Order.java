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

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.INIT;

    private int totalPrice = 0;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    public void updateOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    @PrePersist
    @PreUpdate
    private void calculateTotalPrice() {
        this.totalPrice = this.orderProducts.stream()
                .map(OrderProduct::getProduct)
                .mapToInt(Product::getPrice)
                .sum();
    }
}

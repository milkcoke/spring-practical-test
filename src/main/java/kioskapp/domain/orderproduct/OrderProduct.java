package kioskapp.domain.orderproduct;

import jakarta.persistence.*;
import kioskapp.domain.BaseEntity;
import kioskapp.domain.order.Order;
import kioskapp.domain.product.Product;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class OrderProduct extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    public OrderProduct(Order order, Product product) {
        this.order = order;
        this.product = product;
    }

    public OrderProduct(Product product) {
        this.product = product;
    }

    public void updateOrder(Order order) {
        this.order = order;
    }
}

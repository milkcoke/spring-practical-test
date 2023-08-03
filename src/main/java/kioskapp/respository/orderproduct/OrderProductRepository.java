package kioskapp.respository.orderproduct;

import kioskapp.domain.orderproduct.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
}


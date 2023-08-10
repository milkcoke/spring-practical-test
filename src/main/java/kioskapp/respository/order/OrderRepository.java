package kioskapp.respository.order;

import kioskapp.domain.order.Order;
import kioskapp.domain.order.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("select o from Order o where o.createDateTime >= :startDateTime and o.createDateTime <= :endDateTime and o.orderStatus = :orderStatus")
    List<Order> findOrdersBy(@Param("startDateTime")LocalDateTime startDateTime,
                             @Param("endDateTime") LocalDateTime endDateTime,
                             @Param("orderStatus") OrderStatus orderStatus
    );
}

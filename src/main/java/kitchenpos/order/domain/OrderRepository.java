package kitchenpos.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END " +
           "FROM Order o WHERE o.orderTableId = :orderTableId AND o.orderStatus IN :orderStatus")
    boolean existsByOrderTableIdAndOrderStatusIn(@Param("orderTableId") Long orderTableId,
                                                 @Param("orderStatus") List<OrderStatus> orderStatus);
}

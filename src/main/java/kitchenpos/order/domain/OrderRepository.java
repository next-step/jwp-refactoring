package kitchenpos.order.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END "
        + "FROM Order o WHERE o.orderTableId IN :orderTableIds AND o.orderStatus IN :orderStatus")
    boolean existsByOrderTableIdInAndOrderStatusIn(@Param("orderTableIds") List<Long> orderTableIds,
        @Param("orderStatus") List<OrderStatus> orderStatuses);

    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END"
        + " FROM Order o WHERE o.orderTableId = :orderTableId AND o.orderStatus IN :orderStatus")
    boolean existsByOrderTableIdAndOrderStatusIn(@Param("orderTableId") Long orderTableId,
        @Param("orderStatus") List<OrderStatus> orderStatus);
}

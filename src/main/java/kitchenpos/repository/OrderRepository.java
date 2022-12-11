package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END FROM orders WHERE order_table_id IN (:orderTableIds) AND order_status IN (:orderStatuses)", nativeQuery = true)
    boolean existsByOrderTableIdInAndOrderStatusIn(@Param("orderTableIds") List<Long> orderTableIds,
                                                   @Param("orderStatuses") List<String> orderStatuses);

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END FROM orders WHERE order_table_id = (:orderTableId) AND order_status IN (:orderStatuses)", nativeQuery = true)
    boolean existsByOrderTableIdAndOrderStatusIn(@Param("orderTableId") Long orderTableId,
                                                 @Param("orderStatuses") List<String> asList);
}

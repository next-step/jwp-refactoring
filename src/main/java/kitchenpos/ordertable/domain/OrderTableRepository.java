package kitchenpos.ordertable.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
    List<OrderTable> findAllByIdIn(Iterable<Long> ids);

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END " +
            "FROM orders WHERE order_table_id = (:orderTableId) AND order_status IN (:orderStatuses)", nativeQuery = true)
    boolean findAllById(@Param("orderTableId") Long orderTableId, @Param("orderStatuses") List<String> orderStatuses);

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END " +
            "FROM orders WHERE order_table_id IN (:orderTableIds) AND order_status IN (:orderStatuses)", nativeQuery = true)
    boolean findAllByIdIn(@Param("orderTableIds") List<OrderTable> orderTableIds, @Param("orderStatuses") List<String> orderStatuses);
}
package kitchenpos.order.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kitchenpos.common.domain.OrderStatus;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END FROM Order o WHERE o.orderTableId = :id AND o.orderStatus IN :orderStatuses")
    boolean existsByOrderTableIdAndOrderStatusIn(@Param("id") Long id, @Param("orderStatuses") List<OrderStatus> orderStatuses);

    @Query("SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END FROM Order o WHERE o.orderTableId IN :orderTableIds AND o.orderStatus IN :orderStatuses")
    boolean existsByOrderTableIdInAndOrderStatusIn(@Param("orderTableIds") List<Long> orderTableIds, @Param("orderStatuses") List<OrderStatus> orderStatuses);

    List<Order> findByOrderTableIdIn(List<Long> orderTableIds);

    Optional<Order> findByOrderTableId(Long orderTableId);
}

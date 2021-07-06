package kitchenpos.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kitchenpos.order.domain.Order;

public interface OrderDao extends JpaRepository<Order, Long> {
    @Query("select o from Order o where o.orderTableId in (:orderTableId)")
    List<Order> findByOrderTableId(@Param("orderTableId") Long orderTableId);
}

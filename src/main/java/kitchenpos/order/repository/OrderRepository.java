package kitchenpos.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("select distinct o from Order o "
        + "join fetch o.orderLineItems.orderLineItems ol")
    List<Order> findOrders();

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<OrderStatus> orderStatuses);

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<OrderStatus> orderStatuses);
}

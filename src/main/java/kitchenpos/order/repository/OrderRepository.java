package kitchenpos.order.repository;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByOrderStatusInAndOrderTableIdIn(List<OrderStatus> orderStatus, List<Long> orderTableIds);
    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<OrderStatus> orderStatus);
    List<Order> findAllByOrderTableId(Long orderTableId);
    List<Order> findAllByOrderTableIdIn(List<Long> orderTableIds);
}

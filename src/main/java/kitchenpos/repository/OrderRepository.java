package kitchenpos.repository;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByOrderTableIdInAndOrderStatusIn(Collection<Long> orderTableIds, Collection<OrderStatus> orderStatus);
}

package kitchenpos.order.repository;

import kitchenpos.common.domain.OrderStatus;
import kitchenpos.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<OrderStatus> statusList);

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<OrderStatus> statusList);
}

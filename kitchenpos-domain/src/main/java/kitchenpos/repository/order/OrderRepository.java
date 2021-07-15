package kitchenpos.repository.order;

import kitchenpos.application.order.Order;
import kitchenpos.application.order.OrderStatus;
import kitchenpos.application.ordertable.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<OrderStatus> orderStatuses);

    boolean existsByOrderTableInAndOrderStatusIn(List<OrderTable> orderTableIds, List<OrderStatus> orderStatuses);
}
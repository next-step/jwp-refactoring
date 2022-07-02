package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByOrderTableAndOrderStatusIn(OrderTable orderTable, List<OrderStatus> orderStatus);

    boolean existsByOrderTableIdInAndOrderStatusIn(List<OrderTable> orderTables, List<OrderStatus> orderStatus);
}

package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByOrderTableInAndOrderStatusIn(List<OrderTable> orderTables, List<OrderStatus> asList);
    boolean existsByOrderTableAndOrderStatusIn(OrderTable orderTable, List<OrderStatus> asList);
}

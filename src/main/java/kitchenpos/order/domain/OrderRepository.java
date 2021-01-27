package kitchenpos.order.domain;

import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByOrderTable(OrderTable orderTable);

    List<Order> findByOrderTableIn(List<OrderTable> orderTables);
}

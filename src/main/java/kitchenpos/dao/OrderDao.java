package kitchenpos.dao;

import kitchenpos.domain.order.Order;

import java.util.List;
import kitchenpos.domain.order.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDao extends JpaRepository<Order, Long> {

    List<Order> findAllByOrderTable(OrderTable orderTable);

    List<Order> findAllByOrderTableIn(List<OrderTable> orderTables);
}

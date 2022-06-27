package kitchenpos.order.domain.repository;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.table.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByOrderTable(OrderTable orderTable);

}

package kitchenpos.order.domain;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderTableAndOrderStatusIn(OrderTable orderTable, List<OrderStatus> asList);

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> tableIds, List<OrderStatus> orderStatuses);

}

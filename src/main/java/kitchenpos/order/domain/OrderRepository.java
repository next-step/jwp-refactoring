package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTableEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    boolean existsByOrderTableAndOrderStatusIn(OrderTableEntity orderTable, List<OrderStatus> orderStatuses);

    boolean existsByOrderTableInAndOrderStatusIn(List<OrderTableEntity> orderTables, List<OrderStatus> orderStatuses);
}

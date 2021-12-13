package kitchenpos.order.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.table.domain.OrderTable;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

    boolean existsByOrderTableAndOrderStatusIn(OrderTable orderTable, List<OrderStatus> orderStatuses);

    boolean existsByOrderTableInAndOrderStatusIn(List<OrderTable> orderTables, List<OrderStatus> orderStatuses);
}

package kitchenpos.order.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.table.domain.OrderTable;

public interface OrderRepository extends JpaRepository<Order, Long> {
	boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> asList);

	boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> asList);

	boolean existsByOrderTableInAndOrderStatusIn(List<OrderTable> orderTables, List<OrderStatus> statusList);

	boolean existsByOrderTableAndOrderStatusIn(OrderTable orderTable, List<OrderStatus> statusList);
}

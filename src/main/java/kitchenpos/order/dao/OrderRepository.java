package kitchenpos.order.dao;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByOrderTableAndOrderStatusIn(OrderTable orderTable, List<OrderStatus> orderStatuses);

    boolean existsByOrderTableInAndOrderStatusIn(List<OrderTable> orderTables, List<OrderStatus> orderStatuses);
}

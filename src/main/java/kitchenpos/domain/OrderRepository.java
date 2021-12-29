package kitchenpos.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByOrderTableInAndOrderStatusIn(List<OrderTable> orderTables, List<OrderStatus> statusList);

    boolean existsByOrderTableAndOrderStatusIn(OrderTable orderTable, List<OrderStatus> statusList);
}

package kitchenpos.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByOrderTableIn(List<OrderTable> orderTables);

    boolean existsByOrderTableAndOrderStatusIn(OrderTable orderTable, List<OrderStatus> statusList);
}

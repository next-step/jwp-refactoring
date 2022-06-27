package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    @EntityGraph(attributePaths = {"orderLineItems"})
    List<Orders> findAll();
    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<OrderStatus> orderStatuses);
    boolean existsByOrderTableInAndOrderStatusIn(List<OrderTable> orderTables, List<OrderStatus> orderStatuses);
}

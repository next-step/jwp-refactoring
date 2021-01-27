package kitchenpos.domain.order;

import kitchenpos.domain.table.OrderTable;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findByOrderTable(OrderTable orderTable);
    List<Orders> findByOrderTableIn(Collection<OrderTable> orderTables);
}

package kitchenpos.order.repository;

import java.util.List;
import kitchenpos.order.domain.Orders;
import kitchenpos.table.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    @Query("select (count(o) > 0) from Orders o where o.orderTable.id = ?1 and o.orderStatus <> 'COMPLETION'")
    boolean existNotCompletionOrderTable(Long orderTableId);
    @Query("select (count(o) > 0) from Orders o where o.orderTable in ?1 and o.orderStatus <> 'COMPLETION'")
    boolean existNotCompletionOrderTables(List<OrderTable> orderTables);
}

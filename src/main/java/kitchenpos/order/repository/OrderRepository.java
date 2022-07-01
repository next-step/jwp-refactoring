package kitchenpos.order.repository;

import java.util.List;
import kitchenpos.order.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    @Query("select (count(o) > 0) from Orders o where o.orderTableId = ?1 and o.orderStatus <> 'COMPLETION'")
    boolean existNotCompletionOrderTable(Long orderTableId);

    @Query("select (count(o) > 0) from Orders o where o.orderTableId in ?1 and o.orderStatus <> 'COMPLETION'")
    boolean existNotCompletionOrderTables(List<Long> orderTableIds);
}

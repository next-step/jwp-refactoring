package kitchenpos.order.domain;

import java.util.List;
import java.util.Optional;
import kitchenpos.table.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderDao extends JpaRepository<Order, Long> {

    @Query("SELECT o.orderStatus From Order o where o.orderTable.id = :id")
    Optional<OrderStatus> findByOrderTableId(Long id);

    List<OrderStatus> findByOrderTableIn(List<OrderTable> orderTables);
}

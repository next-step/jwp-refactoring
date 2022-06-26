package kitchenpos.order.repository;

import java.util.List;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.Orders;
import kitchenpos.table.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    boolean existsOrdersByOrderTableIdAndOrderStatusNot(Long orderTableId, OrderStatus orderStatus);
    boolean existsOrdersByOrderTableInAndOrderStatusNot(List<OrderTable> orderTables, OrderStatus orderStatus);
}

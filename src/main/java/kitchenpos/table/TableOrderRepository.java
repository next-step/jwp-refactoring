package kitchenpos.table;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TableOrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderTable_IdAndOrderStatusIn(Long orderTableId, List<OrderStatus> orderStatuses);


    boolean existsByOrderTable_IdInAndOrderStatusIn(List<Long> orderTableId, List<OrderStatus> orderStatuses);

}

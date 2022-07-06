package kitchenpos.order.repository;

import java.util.Collection;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, Collection<OrderStatus> orderStatus);
    boolean existsByOrderTableIdInAndOrderStatusIn(Collection<Long> orderTableId, Collection<OrderStatus> orderStatus);

}

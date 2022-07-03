package kitchenpos.repository;

import java.util.Collection;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, Collection<OrderStatus> orderStatus);
    boolean existsByOrderTableIdInAndOrderStatusIn(Collection<Long> orderTableId, Collection<OrderStatus> orderStatus);

}

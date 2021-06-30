package kitchenpos.order.domain;

import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, Collection<OrderStatus> status);

    boolean existsByOrderTableIdInAndOrderStatusIn(Iterable<Long> orderTableIds, Collection<OrderStatus> status);
}

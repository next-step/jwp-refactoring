package kitchenpos.order.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<OrderStatus> asList);

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<OrderStatus> asList);
}

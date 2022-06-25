package kitchenpos.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<OrderStatus> unCompleteStatus);

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<OrderStatus> asList);
}

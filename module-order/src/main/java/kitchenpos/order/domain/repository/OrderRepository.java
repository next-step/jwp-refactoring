package kitchenpos.order.domain.repository;

import java.util.List;
import kitchenpos.order.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> orderStatus);

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableId,
        List<String> orderStatus);
}

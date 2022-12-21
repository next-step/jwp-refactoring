package kitchenpos.order.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByOrderTableId(Long orderTableId);

    List<Order> findByOrderTableIdIn(List<Long> orderTableIds);
}

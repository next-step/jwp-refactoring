package kitchenpos.order.infrastructure;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Override
    @EntityGraph(attributePaths = {"orderLineItems"})
    List<Order> findAll();
    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> ids, List<OrderStatus> orderStatuses);
    boolean existsByOrderTableIdAndOrderStatusIn(Long id, List<OrderStatus> orderStatuses);
}

package kitchenpos.domain.order;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Override
    @EntityGraph(attributePaths = {"orderLineItems"})
    List<Order> findAll();
    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> ids, List<OrderStatus> orderStatuses);
    boolean existsByOrderTableIdAndOrderStatusIn(Long id, List<OrderStatus> orderStatuses);
}

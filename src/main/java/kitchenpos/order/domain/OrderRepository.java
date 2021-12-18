package kitchenpos.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<OrderStatus> asList);
    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<OrderStatus> asList);
}

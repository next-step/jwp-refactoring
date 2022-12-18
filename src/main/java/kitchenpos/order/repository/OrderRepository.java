package kitchenpos.order.repository;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Orders, Long> {

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<OrderStatus> orderStatuses);

    Optional<Orders> findByOrderTableId(Long orderTableId);
}

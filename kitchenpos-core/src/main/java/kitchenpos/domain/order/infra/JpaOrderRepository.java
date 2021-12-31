package kitchenpos.domain.order.infra;

import kitchenpos.domain.order.domain.Order;
import kitchenpos.domain.order.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaOrderRepository extends JpaRepository<Order, Long> {
    boolean existsByOrderTableIdAndOrderStatusIn(final Long orderTableId, final List<OrderStatus> orderStatuses);

    boolean existsByOrderTableIdInAndOrderStatusIn(final List<Long> orderTableId, final List<OrderStatus> orderStatuses);
}

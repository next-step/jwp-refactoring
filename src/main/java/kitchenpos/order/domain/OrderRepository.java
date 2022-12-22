package kitchenpos.order.domain;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
	boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, Collection<OrderStatus> orderStatuses);

	boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, Collection<OrderStatus> orderStatuses);
}

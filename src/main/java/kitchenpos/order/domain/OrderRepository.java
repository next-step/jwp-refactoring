package kitchenpos.order.domain;

import kitchenpos.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
	boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> orderStatuses);

	boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatuses);
}

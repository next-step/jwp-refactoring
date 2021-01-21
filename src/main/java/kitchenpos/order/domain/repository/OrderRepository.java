package kitchenpos.order.domain.repository;

import kitchenpos.order.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {
	boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> asList);

	boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> asList);

	List<Orders> findAllByOrderTableId(Long id);

}

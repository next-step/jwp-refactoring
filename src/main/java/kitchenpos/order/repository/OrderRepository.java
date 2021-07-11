package kitchenpos.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kitchenpos.order.domain.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findByOrderTableId(Long orderTableId);

	List<Order> findByOrderTableIdIn(List<Long> orderTableIds);
}

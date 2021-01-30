package kitchenpos.order;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
	Optional<Order> findByOrderTableId(Long id);

	List<Order> findAllByOrderTableIdIn(List<Long> ids);
}

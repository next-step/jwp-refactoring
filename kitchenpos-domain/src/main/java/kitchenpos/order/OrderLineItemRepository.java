package kitchenpos.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {
	List<OrderLineItem> findAllByOrderId(Long id);
}

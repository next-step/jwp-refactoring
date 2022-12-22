package kitchenpos.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {

	default OrderLineItem orderLineItem(long id) {
		return findById(id).orElseThrow(
			() -> new IllegalArgumentException(String.format("주문 항목 id(%d)를 찾을 수 없습니다.", id)));
	}
}

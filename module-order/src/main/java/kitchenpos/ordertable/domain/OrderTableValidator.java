package kitchenpos.ordertable.domain;

import java.util.List;

import org.springframework.stereotype.Component;

import kitchenpos.orders.domain.Order;
import kitchenpos.orders.domain.OrderRepository;

@Component
public class OrderTableValidator {

	private final OrderRepository orderRepository;

	public OrderTableValidator(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	public void validateChangeEmpty(OrderTable orderTable) {
		validateOrderTableIsGrouped(orderTable);
		List<Order> orders = orderRepository.findByOrderTableId(orderTable.getId());
		validateOrderIsCompletion(orders);
	}

	private void validateOrderTableIsGrouped(OrderTable orderTable) {
		if (orderTable.isGrouped()) {
			throw new IllegalArgumentException("그룹화 된 테이블은 상태를 변경 할 수 없습니다");
		}
	}

	private void validateOrderIsCompletion(List<Order> orders) {
		if (!isOrderCompletion(orders)) {
			throw new IllegalArgumentException("테이블의 주문이 계산완료 되지 않았습니다");
		}
	}

	private boolean isOrderCompletion(List<Order> orders) {
		return orders.stream()
			.allMatch(order -> order.isCompletion());
	}
}

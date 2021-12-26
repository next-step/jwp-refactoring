package kitchenpos.ordertable.domain;

import org.springframework.stereotype.Component;

@Component
public class OrderTableValidatorImpl implements OrderTableValidator {
	private final Orders orders;

	public OrderTableValidatorImpl(Orders orders) {
		this.orders = orders;
	}

	@Override
	public void validateNotCompletedOrderNotExist(Long id) {
		boolean hasNotCompletedOrder = orders.findByOrderTableId(id)
			.stream()
			.anyMatch(order -> !order.getOrderStatus().isCompletion());

		if (hasNotCompletedOrder) {
			throw new IllegalStateException("완료되지 않은 주문이 남아 있는 경우 빈 상태를 변경할 수 없습니다.");
		}
	}
}

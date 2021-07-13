package kitchenpos.order.domain;

import java.util.List;

import org.springframework.stereotype.Component;

import kitchenpos.table.domain.TableChangeEmptyValidator;
import kitchenpos.table.domain.TableUngroupValidator;

@Component
public class OnlyCompletedOrdersValidator implements TableChangeEmptyValidator, TableUngroupValidator {

	private final OrderRepository orderRepository;

	public OnlyCompletedOrdersValidator(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	@Override
	public void validate(Long orderTableId) {
		final List<Order> orders = orderRepository.findAllByOrderTableId(new OrderTableId(orderTableId));
		validateCompletedOrders(orders);
	}

	private void validateCompletedOrders(List<Order> orders) {
		boolean isCompleteAll = orders.stream().allMatch(Order::isComplete);
		if (!isCompleteAll) {
			throw new IllegalArgumentException("조리상태이거나 식사상태주문의 주문테이블은 상태를 변경할 수 없습니다.");
		}
	}
}

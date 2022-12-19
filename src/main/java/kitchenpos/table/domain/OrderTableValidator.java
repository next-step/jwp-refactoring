package kitchenpos.table.domain;

import java.util.List;

import org.springframework.stereotype.Component;

import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.exception.ErrorMessage;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderValidator;

@Component
public class OrderTableValidator implements OrderValidator {

	private final OrderRepository orderRepository;
	private final OrderTableRepository orderTableRepository;

	public OrderTableValidator(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
		this.orderRepository = orderRepository;
		this.orderTableRepository = orderTableRepository;
	}

	@Override
	public void unGroupOrderStatusValidate(Long orderTableId) {
		if (!isAllFinished(orderTableId)) {
			throw new IllegalArgumentException(ErrorMessage.CANNOT_UNGROUP_WHEN_ORDER_NOT_COMPLETED);
		}
	}

	@Override
	public void changeEmptyOrderStatusValidate(Long orderTableId) {
		if (!isAllFinished(orderTableId)) {
			throw new IllegalArgumentException(ErrorMessage.CANNOT_CHANGE_EMPTINESS_WHEN_ORDER_NOT_COMPLETED);
		}
	}

	@Override
	public void orderTableExistValidate(Long orderTableId) {
		boolean exists = orderTableRepository.existsOrderTableById(orderTableId);
		if (!exists) {
			throw new EntityNotFoundException(OrderTable.ENTITY_NAME, orderTableId);
		}
	}

	@Override
	public void orderTableEmptyValidate(Long orderTableId) {
		OrderTable orderTable = orderTableRepository.findById(orderTableId)
			.orElseThrow(() -> new EntityNotFoundException(OrderTable.ENTITY_NAME, orderTableId));
		if (orderTable.isEmpty()) {
			throw new IllegalArgumentException(ErrorMessage.CANNOT_ORDER_WHEN_TABLE_IS_EMPTY);
		}
	}

	private boolean isAllFinished(Long orderTableId) {
		List<Order> orders = orderRepository.findOrdersByOrderTableId(orderTableId);
		return orders.stream()
			.allMatch(Order::isFinished);
	}
}

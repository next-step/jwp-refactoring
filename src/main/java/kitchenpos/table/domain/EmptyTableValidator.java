package kitchenpos.table.domain;

import static kitchenpos.table.exception.CannotChangeEmptyOrderTable.Type.IN_TABLE_GROUP;
import static kitchenpos.table.exception.CannotChangeEmptyOrderTable.Type.NOT_COMPLETED_ORDER;

import java.util.Arrays;

import org.springframework.stereotype.Component;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.exception.CannotChangeEmptyOrderTable;

@Component
public class EmptyTableValidator {

	private final OrderRepository orderRepository;

	public EmptyTableValidator(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	public void validate(OrderTable orderTable) {
		validateNotInTableGroup(orderTable);
		validateIsCompleted(orderTable);
	}

	private void validateNotInTableGroup(OrderTable orderTable) {
		if (orderTable.hasTableGroup()) {
			throw new CannotChangeEmptyOrderTable(IN_TABLE_GROUP);
		}
	}

	private void validateIsCompleted(OrderTable orderTable) {
		boolean isNotCompleted = orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(),
															 Arrays.asList(OrderStatus.MEAL, OrderStatus.COOKING));
		if (isNotCompleted) {
			throw new CannotChangeEmptyOrderTable(NOT_COMPLETED_ORDER);
		}
	}
}

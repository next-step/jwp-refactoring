package kitchenpos.order.domain;

import static kitchenpos.order.exception.CannotStartOrderException.TYPE.NO_ORDER_ITEMS;
import static kitchenpos.order.exception.CannotStartOrderException.TYPE.ORDER_TABLE_NOT_EMPTY;

import org.springframework.stereotype.Component;

import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.order.exception.CannotStartOrderException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;

@Component
public class OrderValidator {

	private final OrderTableRepository orderTableRepository;

	public OrderValidator(OrderTableRepository orderTableRepository) {
		this.orderTableRepository = orderTableRepository;
	}

	public void validate(Order order) {
		shouldOrderLineItemNotEmpty(order);
		shouldOrderTableEmpty(order);
	}

	private void shouldOrderTableEmpty(Order order) {
		OrderTable orderTable = orderTableRepository.findById(order.getOrderTableId())
													.orElseThrow(EntityNotFoundException::new);
		if (!orderTable.isEmpty()) {
			throw new CannotStartOrderException(ORDER_TABLE_NOT_EMPTY);
		}
	}

	private static void shouldOrderLineItemNotEmpty(Order order) {
		if (order.getOrderLineItems().isEmpty()) {
			throw new CannotStartOrderException(NO_ORDER_ITEMS);
		}
	}
}

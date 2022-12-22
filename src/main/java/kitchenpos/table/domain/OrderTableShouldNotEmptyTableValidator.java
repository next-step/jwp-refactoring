package kitchenpos.table.domain;

import static kitchenpos.order.exception.CannotStartOrderException.TYPE.ORDER_TABLE_NOT_EMPTY;

import kitchenpos.annoation.DomainService;
import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderTableValidator;
import kitchenpos.order.exception.CannotStartOrderException;

@DomainService
public class OrderTableShouldNotEmptyTableValidator implements OrderTableValidator {

	private final OrderTableRepository orderTableRepository;

	public OrderTableShouldNotEmptyTableValidator(OrderTableRepository orderTableRepository) {
		this.orderTableRepository = orderTableRepository;
	}

	@Override
	public void validate(Order order) {
		OrderTable orderTable = getOrderTable(order);
		if (!orderTable.isEmpty()) {
			throw new CannotStartOrderException(ORDER_TABLE_NOT_EMPTY);
		}
	}

	private OrderTable getOrderTable(Order order) {
		return orderTableRepository.findById(order.getOrderTableId())
			.orElseThrow(EntityNotFoundException::new);
	}

}

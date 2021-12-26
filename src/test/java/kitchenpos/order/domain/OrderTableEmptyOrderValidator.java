package kitchenpos.order.domain;

import java.util.List;

import kitchenpos.order.dto.OrderLineItemDto;

public class OrderTableEmptyOrderValidator implements OrderValidator {
	@Override
	public void validateOrderTableExistAndNotEmpty(Long orderTableId) {
		throw new IllegalArgumentException();
	}

	@Override
	public void validateMenusExist(List<OrderLineItemDto> orderLineItemDtos) {
		// do not throw exception
	}
}

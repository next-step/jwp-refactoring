package kitchenpos.order.domain;

import java.util.List;

import kitchenpos.order.dto.OrderLineItemDto;

public class ValidOrderValidator implements OrderValidator {
	@Override
	public void validateOrderTableExistAndNotEmpty(Long orderTableId) {
		// do not throw exception
	}

	@Override
	public void validateMenusExist(List<OrderLineItemDto> orderLineItemDtos) {
		// do not throw exception
	}
}

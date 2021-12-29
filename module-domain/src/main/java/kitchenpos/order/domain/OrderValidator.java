package kitchenpos.order.domain;

import java.util.List;

import kitchenpos.order.dto.OrderLineItemDto;

public interface OrderValidator {
	void validateOrderTableExistAndNotEmpty(Long orderTableId);

	void validateMenusExist(List<OrderLineItemDto> orderLineItemDtos);
}

package kitchenpos.dto;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemRequest {
	private long menuId;

	private long quantity;

	public long getMenuId() {
		return menuId;
	}

	public long getQuantity() {
		return quantity;
	}

	OrderLineItem toEntity() {
		return new OrderLineItem(menuId, quantity);
	}
}

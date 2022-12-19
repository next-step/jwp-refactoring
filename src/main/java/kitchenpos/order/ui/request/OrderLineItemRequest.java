package kitchenpos.order.ui.request;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemRequest {

	private final long menuId;
	private final long quantity;

	public OrderLineItemRequest(long menuId, long quantity) {
		this.menuId = menuId;
		this.quantity = quantity;
	}

	public long getMenuId() {
		return menuId;
	}

	public long getQuantity() {
		return quantity;
	}

	public OrderLineItem toEntity() {
		return OrderLineItem.of(menuId, quantity);
	}
}

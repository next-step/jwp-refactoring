package kitchenpos.order.ui.request;

public class OrderLineItemRequest {

	private final long menuId;
	private final int quantity;

	public OrderLineItemRequest(long menuId, int quantity) {
		this.menuId = menuId;
		this.quantity = quantity;
	}

	public long getMenuId() {
		return menuId;
	}

	public long getQuantity() {
		return quantity;
	}
}

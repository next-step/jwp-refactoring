package kitchenpos.order.ui.request;

public class OrderLineItemRequest {

	private final long menuId;
	private final long quantity;

	public OrderLineItemRequest(long menuId, long quantity) {
		this.menuId = menuId;
		this.quantity = quantity;
	}

	public long menuId() {
		return menuId;
	}

	public long quantity() {
		return quantity;
	}
}

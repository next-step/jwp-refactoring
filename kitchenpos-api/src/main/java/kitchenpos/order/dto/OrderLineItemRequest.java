package kitchenpos.order.dto;

public class OrderLineItemRequest {
	private Long menuId;
	private int quantity;

	public OrderLineItemRequest() {
	}

	public OrderLineItemRequest(final Long menuId, final int quantity) {
		this.menuId = menuId;
		this.quantity = quantity;
	}

	public static OrderLineItemRequest of(final Long menuId, final int quantity) {
		return new OrderLineItemRequest(menuId, quantity);
	}

	public Long getMenuId() {
		return menuId;
	}

	public int getQuantity() {
		return quantity;
	}
}

package kitchenpos.order.dto;

public class OrderLineItemRequest {
	private Long menuId;
	private int quantity;

	private OrderLineItemRequest(Long menuId, int quantity) {
		this.menuId = menuId;
		this.quantity = quantity;
	}

	public static OrderLineItemRequest of(Long menuId, int quantity) {
		return new OrderLineItemRequest(menuId, quantity);
	}

	public Long getMenuId() {
		return menuId;
	}

	public int getQuantity() {
		return quantity;
	}
}

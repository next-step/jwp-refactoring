package kitchenpos.order.dto;

public class OrderLineItemAddRequest {

	private Long menuId;
	private Long quantity;

	protected OrderLineItemAddRequest(){
	}

	private OrderLineItemAddRequest(Long menuId, Long quantity) {
		this.menuId = menuId;
		this.quantity = quantity;
	}

	public static OrderLineItemAddRequest of(Long menuId, Long quantity) {
		return new OrderLineItemAddRequest(menuId, quantity);
	}

	public Long getMenuId() {
		return menuId;
	}

	public Long getQuantity() {
		return quantity;
	}
}

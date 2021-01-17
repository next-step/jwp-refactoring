package kitchenpos.order.dto;

public class OrderLineItemRequest {
	private Long seq;
	private Long orderId;
	private Long menuId;
	private Long quantity;

	public OrderLineItemRequest() {
	}

	public OrderLineItemRequest(Long menuId, Long quantity) {
		this.menuId = menuId;
		this.quantity = quantity;
	}

	public Long getSeq() {
		return seq;
	}

	public void setSeq(Long seq) {
		this.seq = seq;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
}

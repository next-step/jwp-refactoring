package kitchenpos.order.ui.response;

public class OrderLineItemResponse {
	
	private final long seq;
	private final long orderId;
	private final long menuId;
	private final long quantity;

	private OrderLineItemResponse(long seq, long orderId, long menuId, long quantity) {
		this.seq = seq;
		this.orderId = orderId;
		this.menuId = menuId;
		this.quantity = quantity;
	}
	
	public static OrderLineItemResponse of(long seq, long orderId, long menuId, long quantity) {
		return new OrderLineItemResponse(seq, orderId, menuId, quantity);
	}

	public long getSeq() {
		return seq;
	}

	public long getOrderId() {
		return orderId;
	}

	public long getMenuId() {
		return menuId;
	}

	public long getQuantity() {
		return quantity;
	}
}

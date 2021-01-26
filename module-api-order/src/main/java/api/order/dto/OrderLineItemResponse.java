package api.order.dto;

import domain.order.OrderLineItem;

public class OrderLineItemResponse {

	private long seq;
	private long menuId;
	private long quantity;

	OrderLineItemResponse() {
	}

	static OrderLineItemResponse of(OrderLineItem item) {
		return new OrderLineItemResponse(item.getSeq(),
				item.getMenu().getId(),
				item.getQuantity().getValue());
	}

	private OrderLineItemResponse(long seq, long menuId, long quantity) {
		this.seq = seq;
		this.menuId = menuId;
		this.quantity = quantity;
	}

	public long getSeq() {
		return seq;
	}

	public long getMenuId() {
		return menuId;
	}

	public long getQuantity() {
		return quantity;
	}
}

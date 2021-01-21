package kitchenpos.dto;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemResponse {

	private long seq;
	private long orderId;
	private long menuId;
	private long quantity;

	public OrderLineItemResponse() {
	}

	public static OrderLineItemResponse of(OrderLineItem item) {
		return new OrderLineItemResponse(item.getSeq(),
				item.getOrder().getId(),
				item.getMenu().getId(),
				item.getQuantity().getValue());
	}

	public OrderLineItemResponse(long seq, long orderId, long menuId, long quantity) {
		this.seq = seq;
		this.orderId = orderId;
		this.menuId = menuId;
		this.quantity = quantity;
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

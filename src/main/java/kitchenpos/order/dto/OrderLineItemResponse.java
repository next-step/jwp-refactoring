package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {

	private Long seq;
	private Long orderId;
	private Long menuId;
	private long quantity;

	public OrderLineItemResponse() {
	}

	public OrderLineItemResponse(Long seq, Long orderId, Long menuId, long quantity) {
		this.seq = seq;
		this.orderId = orderId;
		this.menuId = menuId;
		this.quantity = quantity;
	}

	public OrderLineItemResponse(OrderLineItem orderLineItem) {
		this.seq = orderLineItem.getSeq();
		this.orderId = orderLineItem.getOrder().getId();
		this.menuId = orderLineItem.getMenu().getId();
		this.quantity = orderLineItem.getQuantity().toLong();
	}

	public Long getSeq() {
		return seq;
	}

	public Long getOrderId() {
		return orderId;
	}

	public Long getMenuId() {
		return menuId;
	}

	public long getQuantity() {
		return quantity;
	}
}

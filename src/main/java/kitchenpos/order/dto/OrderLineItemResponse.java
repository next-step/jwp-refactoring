package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {
	private Long seq;
	private Long orderId;
	private Long menuId;
	private long quantity;

	public OrderLineItemResponse() {
	}

	public OrderLineItemResponse(Long seq, Long id, Long menuId, long quantity) {
		this.seq = seq;
		this.orderId = id;
		this.menuId = menuId;
		this.quantity = quantity;
	}

	public static OrderLineItemResponse of(OrderLineItem orderLineItem) {
		return new OrderLineItemResponse(orderLineItem.getSeq(), orderLineItem.getOrders().getId(),
				orderLineItem.getMenu().getId(), orderLineItem.getQuantity());
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

	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}


}

package kitchenpos.dto.order;


import kitchenpos.domain.order.OrderLineItem;

public class OrderLineItemResponse {
	private Long seq;
	private Long orderId;
	private Long menuId;
	private Long quantity;

	public OrderLineItemResponse() {
	}

	public OrderLineItemResponse(Long seq, Long id, Long menuId, Long quantity) {
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

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}


}

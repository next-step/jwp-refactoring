package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {
	private final Long seq;
	private final Long orderId;
	private final Long menuId;
	private final long quantity;

	private OrderLineItemResponse(final Long seq, final Long orderId, final Long menuId, final long quantity) {
		this.seq = seq;
		this.orderId = orderId;
		this.menuId = menuId;
		this.quantity = quantity;
	}

	public static OrderLineItemResponse of(final Long seq, final Long orderId, final Long menuId, final long quantity) {
		return new OrderLineItemResponse(seq, orderId, menuId, quantity);
	}

	public static OrderLineItemResponse of(final OrderLineItem orderLineItem) {
		return of(orderLineItem.getId(), orderLineItem.getOrder().getId(), orderLineItem.getMenu().getId(),
			orderLineItem.quantity());
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

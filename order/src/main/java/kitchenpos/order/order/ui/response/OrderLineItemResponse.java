package kitchenpos.order.order.ui.response;

import java.util.List;

import kitchenpos.order.order.domain.OrderLineItem;

public class OrderLineItemResponse {

	private final long seq;
	private final long menuId;
	private final long quantity;

	private OrderLineItemResponse(long seq, long menuId, long quantity) {
		this.seq = seq;
		this.menuId = menuId;
		this.quantity = quantity;
	}

	public static OrderLineItemResponse of(long seq, long menuId, long quantity) {
		return new OrderLineItemResponse(seq, menuId, quantity);
	}

	private static OrderLineItemResponse from(OrderLineItem orderLineItem) {
		return new OrderLineItemResponse(
			orderLineItem.seq(),
			orderLineItem.menu().id(),
			orderLineItem.quantity().value()
		);
	}

	public static List<OrderLineItemResponse> listFrom(List<OrderLineItem> orderLineItems) {
		return orderLineItems.stream()
			.map(OrderLineItemResponse::from)
			.collect(java.util.stream.Collectors.toList());
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

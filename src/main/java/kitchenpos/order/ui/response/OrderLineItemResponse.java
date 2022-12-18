package kitchenpos.order.ui.response;

import java.util.List;

import kitchenpos.order.domain.OrderLineItem;

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

	public static List<OrderLineItemResponse> listFrom(List<OrderLineItem> orderLineItems) {
		return orderLineItems.stream()
			.map(orderLineItem -> OrderLineItemResponse.of(orderLineItem.getSeq(), orderLineItem.getOrderId(),
				orderLineItem.getMenuId(), orderLineItem.getQuantity()))
			.collect(java.util.stream.Collectors.toList());
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

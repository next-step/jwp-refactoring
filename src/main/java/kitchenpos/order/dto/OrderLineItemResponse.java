package kitchenpos.order.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {
	private Long seq;
	private Long orderId;
	private Long menuId;
	private long quantity;

	OrderLineItemResponse(OrderLineItem orderLineItem) {
		this.seq = orderLineItem.getId();
		this.orderId = orderLineItem.getOrderId();
		this.menuId = orderLineItem.getMenuId();
		this.quantity = orderLineItem.getQuantity();
	}

	public static List<OrderLineItemResponse> listOf(List<OrderLineItem> orderLineItems) {
		return orderLineItems.stream()
			.map(OrderLineItemResponse::new)
			.collect(Collectors.toList());
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

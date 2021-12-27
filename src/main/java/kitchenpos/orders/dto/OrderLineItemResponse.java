package kitchenpos.orders.dto;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.menu.domain.Menu;
import kitchenpos.orders.domain.Order;
import kitchenpos.orders.domain.OrderLineItem;

public class OrderLineItemResponse {

	private long seq;
	private long orderId;
	private long menuId;
	private long quantity;

	public OrderLineItemResponse() {
	}

	public OrderLineItemResponse(long seq, long orderId, long menuId, long quantity) {
		this.seq = seq;
		this.orderId = orderId;
		this.menuId = menuId;
		this.quantity = quantity;
	}

	public static OrderLineItemResponse of(OrderLineItem orderLineItem) {
		return new OrderLineItemResponse(orderLineItem.getSeq(), orderLineItem.getOrderId(), orderLineItem.getMenuId(),
			orderLineItem.getQuantity());
	}

	public static List<OrderLineItemResponse> ofList(List<OrderLineItem> orderLineItems) {
		return orderLineItems.stream()
			.map(OrderLineItemResponse::of)
			.collect(Collectors.toList());
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

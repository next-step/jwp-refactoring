package kitchenpos.order.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.menu.domain.Menus;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.Orders;

public class OrderRequest {
	private Long orderTableId;
	private List<OrderLineItemRequest> orderLineItems;

	public OrderRequest() {
	}

	public OrderRequest(final Long orderTableId, final List<OrderLineItemRequest> orderLineItems) {
		this.orderTableId = orderTableId;
		this.orderLineItems = orderLineItems;
	}

	public static OrderRequest of(final Long orderTableId, final List<OrderLineItemRequest> orderLineItems) {
		return new OrderRequest(orderTableId, orderLineItems);
	}

	public Long getOrderTableId() {
		return orderTableId;
	}

	public List<OrderLineItemRequest> getOrderLineItems() {
		return orderLineItems;
	}

	public List<Long> getMenuIds() {
		return this.orderLineItems.stream()
			.map(OrderLineItemRequest::getMenuId)
			.collect(Collectors.toList());
	}

	public OrderLineItems toOrderLineItems(Orders order, Menus menus) {
		List<OrderLineItem> result = orderLineItems.stream()
			.map(it -> OrderLineItem.of(order, menus.getMenu(it.getMenuId()), it.getQuantity()))
			.collect(Collectors.toList());
		return OrderLineItems.of(result);
	}
}

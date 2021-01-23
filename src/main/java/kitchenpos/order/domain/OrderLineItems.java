package kitchenpos.order.domain;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.menu.domain.Menus;
import kitchenpos.order.dto.OrderLineItemRequest;

public class OrderLineItems {
	private final List<OrderLineItem> orderLineItems;

	private OrderLineItems(final List<OrderLineItem> orderLineItems) {
		this.orderLineItems = orderLineItems;
	}

	public static OrderLineItems of(final List<OrderLineItem> orderLineItems) {
		return new OrderLineItems(orderLineItems);
	}

	public static OrderLineItems of(Orders order, Menus menus, List<OrderLineItemRequest> orderLineItems) {
		List<OrderLineItem> result = orderLineItems.stream()
			.map(it -> OrderLineItem.of(order, menus.getMenu(it.getMenuId()), it.getQuantity()))
			.collect(Collectors.toList());
		return of(result);
	}

	public List<OrderLineItem> getOrderLineItems() {
		return orderLineItems;
	}
}

package kitchenpos.order.domain;

import java.util.List;

public class OrderLineItems {
	private final List<OrderLineItem> orderLineItems;

	private OrderLineItems(final List<OrderLineItem> orderLineItems) {
		this.orderLineItems = orderLineItems;
	}

	public static OrderLineItems of(final List<OrderLineItem> orderLineItems) {
		return new OrderLineItems(orderLineItems);
	}

	public List<OrderLineItem> getOrderLineItems() {
		return orderLineItems;
	}
}

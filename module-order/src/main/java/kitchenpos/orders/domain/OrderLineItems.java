package kitchenpos.orders.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

public class OrderLineItems {

	private final List<OrderLineItem> orderLineItems;

	public OrderLineItems() {
		this.orderLineItems = new ArrayList<>();
	}

	public OrderLineItems(final List<OrderLineItem> orderLineItems) {
		this.orderLineItems = orderLineItems;
	}

	public OrderLineItems setOrder(Order order) {
		return new OrderLineItems(orderLineItems.stream()
			.map(ol -> new OrderLineItem(order.getId(), ol.getMenuId(), ol.getQuantity()))
			.collect(Collectors.toList()));
	}

	public List<OrderLineItem> value() {
		return new ArrayList<>(orderLineItems);
	}

	public boolean isEmptyOrderLineItems() {
		return CollectionUtils.isEmpty(orderLineItems);
	}
}

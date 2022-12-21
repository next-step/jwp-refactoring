package kitchenpos.order.domain;

import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;

public class OrderLineItems {

	@OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
	private List<OrderLineItem> orderLineItems;

	protected OrderLineItems() {
	}

	private OrderLineItems(List<OrderLineItem> orderLineItems) {
		this.orderLineItems = orderLineItems;
	}

	public static OrderLineItems from(List<OrderLineItem> orderLineItems) {
		return new OrderLineItems(orderLineItems);
	}

	public static OrderLineItems fromSingle(OrderLineItem orderLineItem) {
		return new OrderLineItems(Collections.singletonList(orderLineItem));
	}

	public List<OrderLineItem> list() {
		return Collections.unmodifiableList(orderLineItems);
	}

	public void updateOrder(Order order) {
		orderLineItems.forEach(orderLineItem -> orderLineItem.updateOrder(order));
	}

	public boolean isNotEmpty() {
		return !orderLineItems.isEmpty();
	}
}

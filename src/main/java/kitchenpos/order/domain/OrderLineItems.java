package kitchenpos.order.domain;

import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;

public class OrderLineItems {

	@OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
	private final List<OrderLineItem> orderLineItems;

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
}

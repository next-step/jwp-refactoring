package kitchenpos.order.domain;

import static java.util.Arrays.*;
import static java.util.Objects.*;

import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class OrderLineItems {

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderLineItem> orderLineItems;

	protected OrderLineItems() {}

	private OrderLineItems(List<OrderLineItem> orderLineItems) {
		if (isNull(orderLineItems) || orderLineItems.isEmpty()) {
			throw new IllegalArgumentException("주문 항목이 없습니다.");
		}
		this.orderLineItems = orderLineItems;
	}

	public static OrderLineItems of(List<OrderLineItem> orderLineItems) {
		return new OrderLineItems(orderLineItems);
	}

	public static OrderLineItems of(OrderLineItem... orderLineItems) {
		return new OrderLineItems(asList(orderLineItems));
	}

	void toOrder(Order order) {
		for (OrderLineItem orderLineItem : orderLineItems) {
			orderLineItem.toOrder(order);
		}
	}

	List<OrderLineItem> getOrderLineItems() {
		return Collections.unmodifiableList(orderLineItems);
	}
}

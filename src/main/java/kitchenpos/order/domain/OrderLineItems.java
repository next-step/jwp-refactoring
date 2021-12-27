package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

public class OrderLineItems {

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.PERSIST)
	private List<OrderLineItem> orderLineItems = new ArrayList<>();

	public OrderLineItems() {
	}

	private OrderLineItems(List<OrderLineItem> orderLineItems) {
		this.orderLineItems = orderLineItems;
	}

	public static OrderLineItems of(List<OrderLineItem> orderLineItemList) {
		return new OrderLineItems(orderLineItemList);
	}

	public static OrderLineItems empty() {
		return of(new ArrayList<>());
	}

	public void add(OrderLineItem item) {
		orderLineItems.add(item);
	}

	public List<OrderLineItem> toList() {
		return Collections.unmodifiableList(orderLineItems);
	}
}

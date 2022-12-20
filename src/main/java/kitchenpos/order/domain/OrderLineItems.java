package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public class OrderLineItems {

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "order_id")
	private List<OrderLineItem> orderLineItems = new ArrayList<>();

	protected OrderLineItems() {
	}

	public OrderLineItems(List<OrderLineItem> orderLineItems) {
		this.orderLineItems = orderLineItems;
	}

	public void addAll(OrderLineItems others) {
		orderLineItems.addAll(others.orderLineItems);
	}

	public boolean isEmpty() {
		return orderLineItems.isEmpty();
	}

	public Stream<OrderLineItem> stream() {
		return orderLineItems.stream();
	}

	public List<OrderLineItem> toList() {
		return Collections.unmodifiableList(orderLineItems);
	}
}

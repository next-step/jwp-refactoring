package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class OrderLineItems {

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<OrderLineItem> orderLineItems = new ArrayList<>();

	protected OrderLineItems() {
	}

	public void addAll(Order order, Map<Long, Integer> menus) {
		orderLineItems.addAll(OrderLineItem.of(order, menus));
	}

	public boolean isEmpty() {
		return orderLineItems.isEmpty();
	}

	public Stream<OrderLineItem> stream() {
		return orderLineItems.stream();
	}
}

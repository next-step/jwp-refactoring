package kitchenpos.orders.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class OrderLineItems {

	@OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderLineItem> orderLineItems;

	public OrderLineItems() {
	}

	public OrderLineItems(final List<OrderLineItem> orderLineItems) {
		this.orderLineItems = orderLineItems;
	}

	public OrderLineItems setOrder(Order order) {
		return new OrderLineItems(orderLineItems.stream()
			.map(ol -> new OrderLineItem(order, ol.getMenu(), ol.getQuantity()))
			.collect(Collectors.toList()));
	}

	public List<OrderLineItem> value() {
		return new ArrayList<>(orderLineItems);
	}
}

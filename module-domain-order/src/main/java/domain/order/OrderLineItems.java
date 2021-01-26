package domain.order;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class OrderLineItems implements Iterable<OrderLineItem> {

	static final String MSG_CANNOT_CREATE_EMPTY_ITEMS = "Cannot create Order By empty OrderItems";

	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "order_id", nullable = false)
	private List<OrderLineItem> orderLineItems;

	OrderLineItems() {
		orderLineItems = new ArrayList<>();
	}

	OrderLineItems(List<OrderLineItem> orderLineItems) {
		this.orderLineItems = orderLineItems;
	}

	static OrderLineItems of(List<OrderItem> items) {
		if (items.isEmpty()) {
			throw new OrderValidationException(MSG_CANNOT_CREATE_EMPTY_ITEMS);
		}
		return new OrderLineItems(getOrderLineItems(items));
	}

	private static List<OrderLineItem> getOrderLineItems(List<OrderItem> items) {
		return items.stream()
				.map(OrderItem::toOrderLineItem)
				.collect(Collectors.toList());
	}

	@Override
	public Iterator<OrderLineItem> iterator() {
		return orderLineItems.iterator();
	}
}

package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import org.springframework.util.CollectionUtils;

import kitchenpos.order.exception.NotFoundOrderLineItemsException;

@Embeddable
public class OrderLineItems {

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderLineItem> orderLineItems = new ArrayList<>();

	protected OrderLineItems() {
	}

	private OrderLineItems(Order order, List<OrderLineItem> orderLineItems) {
		validate(orderLineItems);
		addAll(order, orderLineItems);
	}

	private void validate(List<OrderLineItem> orderLineItems) {
		if (CollectionUtils.isEmpty(orderLineItems)) {
			throw new NotFoundOrderLineItemsException();
		}
	}

	public static OrderLineItems of(Order order, List<OrderLineItem> orderLineItems) {
		return new OrderLineItems(order, orderLineItems);
	}

	private void addAll(Order order, List<OrderLineItem> orderLineItems) {
		this.orderLineItems.addAll(orderLineItems);
		this.orderLineItems.forEach(orderLineItem -> orderLineItem.setOrder(order));
	}

	public List<OrderLineItem> getOrderLineItems() {
		return Collections.unmodifiableList(orderLineItems);
	}
}

package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.springframework.util.CollectionUtils;

import kitchenpos.order.exception.NotFoundOrderLineItemsException;

@Embeddable
public class OrderLineItems {

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "order_id")
	private List<OrderLineItem> orderLineItems = new ArrayList<>();

	protected OrderLineItems() {
	}

	private OrderLineItems(List<OrderLineItem> orderLineItems) {
		validate(orderLineItems);
		addAll(orderLineItems);
	}

	private void validate(List<OrderLineItem> orderLineItems) {
		if (CollectionUtils.isEmpty(orderLineItems)) {
			throw new NotFoundOrderLineItemsException();
		}
	}

	public static OrderLineItems of(List<OrderLineItem> orderLineItems) {
		return new OrderLineItems(orderLineItems);
	}

	private void addAll(List<OrderLineItem> orderLineItems) {
		this.orderLineItems.addAll(orderLineItems);
	}

	public List<OrderLineItem> getOrderLineItems() {
		return Collections.unmodifiableList(orderLineItems);
	}
}

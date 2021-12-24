package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import org.springframework.util.CollectionUtils;

import kitchenpos.order.exception.InvalidOrderException;

@Embeddable
public class OrderLineItems {

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderLineItem> orderLineItems = new ArrayList<>();

	protected OrderLineItems() {
	}

	private OrderLineItems(List<OrderLineItem> orderLineItems) {
		validate(orderLineItems);
		this.orderLineItems = orderLineItems;
	}

	private void validate(List<OrderLineItem> orderLineItems) {
		if (CollectionUtils.isEmpty(orderLineItems)) {
			throw new InvalidOrderException("주문항목 목록이 있어야 합니다.");
		}
	}

	public static OrderLineItems of() {
		return of(new ArrayList<>());
	}

	public static OrderLineItems of(List<OrderLineItem> orderLineItems) {
		return new OrderLineItems(orderLineItems);
	}

	public void addAll(Order order, List<OrderLineItem> orderLineItems) {
		this.orderLineItems.addAll(orderLineItems);
		this.orderLineItems.forEach(orderLineItem -> orderLineItem.setOrder(order));
	}

	public List<OrderLineItem> getOrderLineItems() {
		return Collections.unmodifiableList(orderLineItems);
	}
}

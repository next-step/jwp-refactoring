package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import kitchenpos.common.ErrorCode;
import kitchenpos.order.exception.OrderException;

@Embeddable
public class OrderLineItems {

	public static final OrderLineItems EMPTY_ITEMS = new OrderLineItems();

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<OrderLineItem> orderLineItems = new ArrayList<>();

	protected OrderLineItems() {
	}

	private OrderLineItems(List<OrderLineItem> orderLineItems) {
		this.orderLineItems = orderLineItems;
	}

	public static OrderLineItems of(List<OrderLineItem> orderLineItems) {
		validateOrderLineItems(orderLineItems);
		return new OrderLineItems(orderLineItems);
	}

	private static void validateOrderLineItems(List<OrderLineItem> savedOrderLineItems) {
		if (savedOrderLineItems == null || savedOrderLineItems.isEmpty()) {
			throw new OrderException(ErrorCode.ORDER_LINE_ITEMS_IS_EMPTY);
		}
	}

	public List<OrderLineItem> getOrderLineItems() {
		return orderLineItems;
	}
}

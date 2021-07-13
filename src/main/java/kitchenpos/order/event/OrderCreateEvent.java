package kitchenpos.order.event;

import java.util.List;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.dto.OrderLineItemRequest;

public class OrderCreateEvent {

	private Order order;
	private List<OrderLineItemRequest> orderLineItemRequests;

	public OrderCreateEvent(Order order, List<OrderLineItemRequest> orderLineItemRequests) {
		this.order = order;
		this.orderLineItemRequests = orderLineItemRequests;
	}

	public Order getOrder() {
		return order;
	}

	public List<OrderLineItemRequest> getOrderLineItemRequests() {
		return orderLineItemRequests;
	}
}

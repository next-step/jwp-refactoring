package kitchenpos.order.domain.event;

import kitchenpos.order.domain.Order;

public class OrderCreatedEvent {

	private final Order order;

	private OrderCreatedEvent(Order order) {
		this.order = order;
	}

	public static OrderCreatedEvent from(Order order) {
		return new OrderCreatedEvent(order);
	}

	public long tableId() {
		return order.orderTableId();
	}

}

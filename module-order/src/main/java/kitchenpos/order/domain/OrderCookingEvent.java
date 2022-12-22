package kitchenpos.order.domain;

public class OrderCookingEvent {

	private final Order order;

	public OrderCookingEvent(Order order) {
		this.order = order;
	}

	public Long getOrderTableId() {
		return order.getOrderTableId();
	}
}

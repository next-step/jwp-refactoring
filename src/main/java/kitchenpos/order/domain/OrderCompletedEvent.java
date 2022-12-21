package kitchenpos.order.domain;

public class OrderCompletedEvent {

	private final Order order;

	public OrderCompletedEvent(Order order) {
		this.order = order;
	}

	public Long getOrderTableId() {
		return order.getOrderTableId();
	}
}

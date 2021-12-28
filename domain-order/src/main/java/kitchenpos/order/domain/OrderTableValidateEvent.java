package kitchenpos.order.domain;

public class OrderTableValidateEvent {

	private Long orderTableId;

	protected OrderTableValidateEvent() {
	}

	private OrderTableValidateEvent(Long orderTableId) {
		this.orderTableId = orderTableId;
	}

	public static OrderTableValidateEvent from(Long orderTableId) {
		return new OrderTableValidateEvent(orderTableId);
	}

	public Long getOrderTableId() {
		return orderTableId;
	}
}

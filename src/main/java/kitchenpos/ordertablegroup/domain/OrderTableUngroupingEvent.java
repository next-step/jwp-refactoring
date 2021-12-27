package kitchenpos.ordertablegroup.domain;

public class OrderTableUngroupingEvent {
	private final Long orderTableGroupId;

	public OrderTableUngroupingEvent(Long orderTableGroupId) {
		this.orderTableGroupId = orderTableGroupId;
	}

	public Long getOrderTableGroupId() {
		return orderTableGroupId;
	}
}

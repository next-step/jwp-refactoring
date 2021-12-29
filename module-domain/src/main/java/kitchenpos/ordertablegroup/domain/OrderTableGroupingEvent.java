package kitchenpos.ordertablegroup.domain;

import java.util.List;

public class OrderTableGroupingEvent {
	private final Long orderTableGroupId;
	private final List<Long> orderTableIds;

	public OrderTableGroupingEvent(Long orderTableGroupId, List<Long> orderTableIds) {
		this.orderTableGroupId = orderTableGroupId;
		this.orderTableIds = orderTableIds;
	}

	public Long getOrderTableGroupId() {
		return orderTableGroupId;
	}

	public List<Long> getOrderTableIds() {
		return orderTableIds;
	}
}

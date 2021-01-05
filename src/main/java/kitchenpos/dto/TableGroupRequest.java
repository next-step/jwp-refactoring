package kitchenpos.dto;

import java.util.List;

public class TableGroupRequest {
	private List<Long> orderTableIds;

	private TableGroupRequest() {
	}

	private TableGroupRequest(List<Long> orderTableIds) {
		this.orderTableIds = orderTableIds;
	}

	public static TableGroupRequest of(List<Long> orderTableIds) {
		return new TableGroupRequest(orderTableIds);
	}

	public List<Long> getOrderTableIds() {
		return orderTableIds;
	}
}

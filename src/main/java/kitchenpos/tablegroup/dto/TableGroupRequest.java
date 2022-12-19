package kitchenpos.tablegroup.dto;

import java.util.List;

public class TableGroupRequest {

	private List<Long> orderTableIds;

	protected TableGroupRequest() {}

	private TableGroupRequest(List<Long> orderTableIds) {
		this.orderTableIds = orderTableIds;
	}

	public static TableGroupRequest of(List<Long> orderTableRequests) {
		return new TableGroupRequest(orderTableRequests);
	}

	public List<Long> getOrderTableIds() {
		return orderTableIds;
	}

}

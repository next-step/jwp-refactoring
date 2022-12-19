package kitchenpos.table.dto;

import java.util.List;

public class TableGroupRequest {

	private List<Long> orderTableRequests;

	protected TableGroupRequest() {}

	private TableGroupRequest(List<Long> orderTableRequests) {
		this.orderTableRequests = orderTableRequests;
	}

	public static TableGroupRequest of(List<Long> orderTableRequests) {
		return new TableGroupRequest(orderTableRequests);
	}

	public List<Long> getOrderTableRequests() {
		return orderTableRequests;
	}

}

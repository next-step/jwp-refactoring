package kitchenpos.tablegroup.dto;

import java.util.List;

public class TableGroupAddRequest {

	private List<Long> orderTableIds;

	protected TableGroupAddRequest() {
	}

	private TableGroupAddRequest(List<Long> orderTableIds) {
		this.orderTableIds = orderTableIds;
	}

	public static TableGroupAddRequest of(List<Long> orderTableIds) {
		return new TableGroupAddRequest(orderTableIds);
	}

	public List<Long> getOrderTableIds() {
		return orderTableIds;
	}
}

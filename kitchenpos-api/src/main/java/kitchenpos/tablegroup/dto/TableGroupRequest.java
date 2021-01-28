package kitchenpos.tablegroup.dto;

import java.util.List;

public class TableGroupRequest {
	private List<Long> orderTables;

	public TableGroupRequest() {
	}

	public TableGroupRequest(final List<Long> orderTables) {
		this.orderTables = orderTables;
	}

	public static TableGroupRequest of(final List<Long> orderTables) {
		return new TableGroupRequest(orderTables);
	}

	public List<Long> getOrderTables() {
		return orderTables;
	}
}

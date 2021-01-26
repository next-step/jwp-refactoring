package api.order.dto;

import java.util.List;

public class TableGroupRequest_Create {

	private List<Long> orderTables;

	public TableGroupRequest_Create() {
	}

	public TableGroupRequest_Create(List<Long> orderTables) {
		this.orderTables = orderTables;
	}

	public List<Long> getOrderTables() {
		return orderTables;
	}
}

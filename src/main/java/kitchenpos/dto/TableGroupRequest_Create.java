package kitchenpos.dto;

import java.util.List;

public class TableGroupRequest_Create {

	private List<Long> tableIds;

	public TableGroupRequest_Create() {
	}

	public TableGroupRequest_Create(List<Long> tableIds) {
		this.tableIds = tableIds;
	}

	public List<Long> getTableIds() {
		return tableIds;
	}
}

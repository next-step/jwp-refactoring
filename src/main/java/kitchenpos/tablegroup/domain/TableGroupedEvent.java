package kitchenpos.tablegroup.domain;

import java.util.List;

public class TableGroupedEvent {

	private final List<Long> orderTableIds;
	private final Long tableGroupId;

	public TableGroupedEvent(Long tableGroupId, List<Long> orderTableIds) {
		this.tableGroupId = tableGroupId;
		this.orderTableIds = orderTableIds;
	}

	public List<Long> getOrderTableIds() {
		return orderTableIds;
	}

	public Long getTableGroupId() {
		return tableGroupId;
	}
}

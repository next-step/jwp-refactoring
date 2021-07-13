package kitchenpos.table.event;

import java.util.List;

import kitchenpos.table.domain.TableGroup;

public class OrderTableGroupEvent {

	private TableGroup tableGroup;
	private List<Long> orderTableIds;

	public OrderTableGroupEvent(TableGroup tableGroup, List<Long> orderTableIds) {
		this.tableGroup = tableGroup;
		this.orderTableIds = orderTableIds;
	}

	public TableGroup getTableGroup() {
		return tableGroup;
	}

	public List<Long> getOrderTableIds() {
		return orderTableIds;
	}
}

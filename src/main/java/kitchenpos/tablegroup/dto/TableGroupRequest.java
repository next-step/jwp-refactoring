package kitchenpos.tablegroup.dto;

import java.util.List;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;

public class TableGroupRequest {

	private List<Long> orderTableIds;

	public TableGroupRequest(List<Long> orderTableIds) {
		this.orderTableIds = orderTableIds;
	}

	public List<Long> getOrderTableIds() {
		return orderTableIds;
	}

	public TableGroup toTableGroup(List<OrderTable> orderTables) {
		return new TableGroup(orderTables);
	}
}

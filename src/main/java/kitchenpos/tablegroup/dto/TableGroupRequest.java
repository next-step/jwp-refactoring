package kitchenpos.tablegroup.dto;

import java.util.List;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;

public class TableGroupRequest {

	private List<Long> orderTables;

	protected TableGroupRequest() {
	}

	private TableGroupRequest(List<Long> orderTables) {
		this.orderTables = orderTables;
	}

	public static TableGroupRequest of(List<Long> orderTables) {
		return new TableGroupRequest(orderTables);
	}

	public List<Long> getOrderTables() {
		return orderTables;
	}

	public TableGroup toEntity(List<OrderTable> savedOrderTables) {
		return TableGroup.from(savedOrderTables);
	}
}

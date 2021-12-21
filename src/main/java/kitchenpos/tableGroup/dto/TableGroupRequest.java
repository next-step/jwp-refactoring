package kitchenpos.tableGroup.dto;

import java.util.List;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.tableGroup.domain.TableGroup;

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

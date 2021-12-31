package kitchenpos.tablegroup.dto;

import java.util.List;

import kitchenpos.ordertable.domain.domain.OrderTable;
import kitchenpos.tablegroup.domain.domain.TableGroup;

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

	public TableGroup toEntity(List<OrderTable> orderTables) {
		return TableGroup.of(orderTables);
	}

	public List<Long> getOrderTableIds() {
		return orderTableIds;
	}
}

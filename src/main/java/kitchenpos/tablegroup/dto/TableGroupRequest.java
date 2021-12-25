package kitchenpos.tablegroup.dto;

import java.util.List;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.tablegroup.domain.TableGroup;

public class TableGroupRequest {
	private List<OrderTableRequest> orderTableRequests;

	public TableGroupRequest() {
	}

	public TableGroupRequest(List<OrderTableRequest> orderTableRequests) {
		this.orderTableRequests = orderTableRequests;
	}

	public List<OrderTableRequest> getOrderTableRequests() {
		return orderTableRequests;
	}


	public TableGroup toTableGroup(List<OrderTable> orderTables) {
		return new TableGroup(orderTables);
	}
}

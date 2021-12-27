package kitchenpos.tablegroup.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.tablegroup.domain.OrderTables;
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


	public TableGroup toTableGroup(OrderTables orderTables) {
		return new TableGroup(orderTables);
	}

	public List<Long> extractOrderTableIds() {
		return orderTableRequests
			.stream()
			.map(orderTableRequest -> orderTableRequest.getId())
			.collect(Collectors.toList());
	}
}

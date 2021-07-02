package kitchenpos.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.OrderTable;

public class TableGroupRequest {
	private List<OrderTableRequest> orderTables;

	public TableGroupRequest() {}

	public TableGroupRequest(List<OrderTableRequest> orderTables) {
		this.orderTables = orderTables;
	}

	public List<OrderTableRequest> getOrderTables() {
		return orderTables;
	}

	public List<Long> getOrderTableIds() {
		return orderTables.stream()
			.map(OrderTableRequest::getId)
			.collect(Collectors.toList());
	}
}

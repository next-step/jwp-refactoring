package kitchenpos.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.OrderTable;

public class TableGroupRequest {
	private List<OrderTableRequest> orderTables;

	public List<Long> getOrderTableIds() {
		return orderTables.stream()
			.map(OrderTableRequest::getId)
			.collect(Collectors.toList());
	}
}

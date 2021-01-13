package kitchenpos.order.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TableGroupRequest {
	private List<OrderTableRequest> orderTables = new ArrayList<>();

	public TableGroupRequest() {
	}

	public TableGroupRequest(List<OrderTableRequest> orderTables) {
		if (Objects.nonNull(orderTables)) {
			this.orderTables = orderTables;
		}
	}

	public List<OrderTableRequest> getOrderTables() {
		return orderTables;
	}

	public int orderTableSize() {
		return this.orderTables.size();
	}

	public List<Long> orderTableIds() {
		return this.orderTables.stream()
			.map(OrderTableRequest::getId)
			.collect(Collectors.toList());
	}
}

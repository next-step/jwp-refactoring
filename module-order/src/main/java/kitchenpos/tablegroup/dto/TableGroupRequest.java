package kitchenpos.tablegroup.dto;

import java.util.List;
import java.util.stream.Collectors;

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

	public TableGroup toTableGroup() {
		return new TableGroup();
	}

	public List<Long> extractOrderTableIds() {
		return orderTableRequests
			.stream()
			.map(orderTableRequest -> orderTableRequest.getId())
			.collect(Collectors.toList());
	}
}

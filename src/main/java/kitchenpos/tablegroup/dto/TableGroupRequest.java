package kitchenpos.tablegroup.dto;

import java.util.Set;

public class TableGroupRequest {

	private Long id;
	private Set<Long> orderTableIds;

	public TableGroupRequest() {
	}

	public TableGroupRequest(Set<Long> orderTableIds) {
		this.orderTableIds = orderTableIds;
	}

	public Long getId() {
		return id;
	}

	public Set<Long> getOrderTableIds() {
		return orderTableIds;
	}

	public int getRequestSize() {
		return orderTableIds.size();
	}
}

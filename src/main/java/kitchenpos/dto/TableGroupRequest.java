package kitchenpos.dto;

import java.util.List;

public class TableGroupRequest {
	private List<Long> orderTableIds;

	public List<Long> getOrderTableIds() {
		return orderTableIds;
	}

	public void setOrderTableIds(List<Long> orderTableIds) {
		this.orderTableIds = orderTableIds;
	}
}

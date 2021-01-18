package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderTables;
import kitchenpos.order.domain.TableGroup;

import java.util.Set;

public class TableGroupRequest {
	private Set<Long> orderTableIds;

	public TableGroupRequest() {
	}

	public TableGroupRequest(Set<Long> orderTableIds) {
		this.orderTableIds = orderTableIds;
	}

	public Set<Long> getOrderTableIds() {
		return orderTableIds;
	}

	public void setOrderTableIds(Set<Long> orderTableIds) {
		this.orderTableIds = orderTableIds;
	}

	public TableGroup toEntity(OrderTables orderTables) {
		return new TableGroup(orderTables);
	}
}

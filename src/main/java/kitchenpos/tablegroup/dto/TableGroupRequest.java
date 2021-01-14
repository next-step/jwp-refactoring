package kitchenpos.tablegroup.dto;

import java.util.Set;
import kitchenpos.ordertable.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;

public class TableGroupRequest {

	private Long id;
	private Set<Long> orderTableIds;

	public TableGroupRequest(Set<Long> orderTableIds) {
		this.orderTableIds = orderTableIds;
	}

	public TableGroup toEntity(OrderTables orderTables) {
		return new TableGroup(orderTables);
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

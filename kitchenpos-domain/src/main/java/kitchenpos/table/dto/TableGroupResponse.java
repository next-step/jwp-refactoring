package kitchenpos.table.dto;

import java.util.List;

import kitchenpos.table.TableGroup;

public class TableGroupResponse {
	private Long id;
	private List<OrderTableResponse> orderTables;

	protected TableGroupResponse() {
	}

	public TableGroupResponse(Long id, List<OrderTableResponse> orderTables) {
		this.id = id;
		this.orderTables = orderTables;
	}

	public static TableGroupResponse of(TableGroup tableGroup) {
		return new TableGroupResponse(tableGroup.getId(), OrderTableResponse.of(tableGroup.getOrderTables()));
	}

	public Long getId() {
		return id;
	}

	public List<OrderTableResponse> getOrderTables() {
		return orderTables;
	}
}

package kitchenpos.ui.dto;

import java.util.List;

import kitchenpos.domain.TableGroup2;

public class TableGroupResponse {

	private Long id;
	private List<OrderTableResponse> orderTables;

	private TableGroupResponse() {
	}

	public TableGroupResponse(Long id, List<OrderTableResponse> orderTables) {
		this.id = id;
		this.orderTables = orderTables;
	}

	public TableGroupResponse(TableGroup2 tableGroup) {
		this(tableGroup.getId(), OrderTableResponse.of(tableGroup.getOrderTables()));
	}

	public Long getId() {
		return id;
	}

	public List<OrderTableResponse> getOrderTables() {
		return orderTables;
	}
}

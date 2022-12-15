package kitchenpos.ui.dto;

import java.util.List;

public class TableGroupResponse {

	private Long id;
	private List<OrderTableResponse> orderTables;

	private TableGroupResponse() {
	}

	public TableGroupResponse(Long id, List<OrderTableResponse> orderTables) {
		this.id = id;
		this.orderTables = orderTables;
	}

	public Long getId() {
		return id;
	}

	public List<OrderTableResponse> getOrderTables() {
		return orderTables;
	}
}

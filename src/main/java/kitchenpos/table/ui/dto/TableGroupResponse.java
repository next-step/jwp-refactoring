package kitchenpos.table.ui.dto;

import java.util.List;

import kitchenpos.table.domain.TableGroup;

public class TableGroupResponse {

	private Long id;
	private List<OrderTableResponse> orderTables;

	private TableGroupResponse() {
	}

	public TableGroupResponse(Long id, List<OrderTableResponse> orderTables) {
		this.id = id;
		this.orderTables = orderTables;
	}

	public TableGroupResponse(TableGroup tableGroup) {
		this(tableGroup.getId(),
			 OrderTableResponse.of(
				 tableGroup.getOrderTables().toList()));
	}

	public Long getId() {
		return id;
	}

	public List<OrderTableResponse> getOrderTables() {
		return orderTables;
	}
}

package kitchenpos.tablegroup.dto;

import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.tablegroup.domain.TableGroup;

public class TableGroupResponse {

	private Long id;

	private LocalDateTime createdDate;

	private List<OrderTableResponse> orderTables;

	TableGroupResponse(TableGroup tableGroup, List<OrderTableResponse> orderTables) {
		this.id = tableGroup.getId();
		this.createdDate = tableGroup.getCreatedDate();
		this.orderTables = orderTables;
	}

	public static TableGroupResponse of(TableGroup tableGroup, List<OrderTable> orderTables) {
		List<OrderTableResponse> orderTableResponses = OrderTableResponse.listOf(orderTables);
		return new TableGroupResponse(tableGroup, orderTableResponses);
	}

	public Long getId() {
		return id;
	}
}

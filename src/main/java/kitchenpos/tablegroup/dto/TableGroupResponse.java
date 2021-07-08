package kitchenpos.tablegroup.dto;

import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.dto.OrderTableResponse;
import kitchenpos.tablegroup.domain.TableGroup;

public class TableGroupResponse {

	private Long id;

	private LocalDateTime createdDate;

	private List<OrderTableResponse> orderTables;

	TableGroupResponse(Long id, LocalDateTime createdDate, List<OrderTableResponse> orderTables) {
		this.id = id;
		this.createdDate = createdDate;
		this.orderTables = orderTables;
	}

	public static TableGroupResponse of(TableGroup tableGroup) {
		List<OrderTableResponse> orderTableResponses = OrderTableResponse.listOf(tableGroup.getOrderTables());
		return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(), orderTableResponses);
	}

	public Long getId() {
		return id;
	}
}

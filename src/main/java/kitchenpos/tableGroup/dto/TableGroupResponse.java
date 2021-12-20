package kitchenpos.tableGroup.dto;

import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.order.dto.OrderTableResponse;
import kitchenpos.tableGroup.domain.TableGroup;

public class TableGroupResponse {
	private Long id;
	private List<OrderTableResponse> orderTables;
	private LocalDateTime createdDate;

	protected TableGroupResponse() {
	}

	private TableGroupResponse(Long id, List<OrderTableResponse> orderTables,
		LocalDateTime createdDate) {
		this.id = id;
		this.orderTables = orderTables;
		this.createdDate = createdDate;
	}

	public static TableGroupResponse from(TableGroup tableGroup) {
		return new TableGroupResponse(tableGroup.getId(), OrderTableResponse.ofList(tableGroup.getOrderTables()),
			tableGroup.getCreatedDate());
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public List<OrderTableResponse> getOrderTables() {
		return orderTables;
	}
}

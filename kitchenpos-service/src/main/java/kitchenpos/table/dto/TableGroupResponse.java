package kitchenpos.table.dto;

import kitchenpos.table.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupResponse {
	private Long id;
	private LocalDateTime createdDate;
	private List<OrderTableResponse> orderTableResponses;

	protected TableGroupResponse() {
	}

	public TableGroupResponse(Long id, LocalDateTime createdDate, List<OrderTableResponse> orderTableResponses) {
		this.id = id;
		this.createdDate = createdDate;
		this.orderTableResponses = orderTableResponses;
	}

	public static TableGroupResponse of(TableGroup tableGroup) {
		return new TableGroupResponse(
				tableGroup.getId(),
				tableGroup.getCreatedDate(),
				OrderTableResponse.of(tableGroup.getOrderTables()));
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public List<OrderTableResponse> getOrderTableResponses() {
		return orderTableResponses;
	}
}

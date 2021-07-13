package kitchenpos.table.dto;

import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.table.domain.TableGroup;

public class TableGroupResponse {

	private Long id;
	private List<OrderTableResponse> orderTableResponses;
	private LocalDateTime createdDate;

	public TableGroupResponse(Long id, List<OrderTableResponse> orderTableResponses, LocalDateTime createdDate) {
		this.id = id;
		this.orderTableResponses = orderTableResponses;
		this.createdDate = createdDate;
	}

	public Long getId() {
		return id;
	}

	public List<OrderTableResponse> getOrderTableResponses() {
		return orderTableResponses;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public static TableGroupResponse of(TableGroup tableGroup) {
		return new TableGroupResponse(tableGroup.getId(), OrderTableResponse.of(tableGroup.getOrderTables()), tableGroup.getCreatedDate());
	}
}

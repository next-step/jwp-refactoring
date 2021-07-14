package kitchenpos.order.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.order.domain.TableGroup;

public class TableGroupResponse {

	private Long id;
	private List<OrderTableResponse> orderTables;
	private LocalDateTime createdDate;

	public TableGroupResponse() {
	}

	public TableGroupResponse(Long id, List<OrderTableResponse> orderTables, LocalDateTime createdDate) {
		this.id = id;
		this.orderTables = orderTables;
		this.createdDate = createdDate;
	}

	public static TableGroupResponse of(TableGroup tableGroup) {
		List<OrderTableResponse> orderTableResponses = tableGroup.getOrderTables()
			.stream()
			.map(OrderTableResponse::of)
			.collect(Collectors.toList());
		return new TableGroupResponse(tableGroup.getId(), orderTableResponses, tableGroup.getCreatedDate());
	}

	public Long getId() {
		return id;
	}

	public List<OrderTableResponse> getOrderTables() {
		return orderTables;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}
}

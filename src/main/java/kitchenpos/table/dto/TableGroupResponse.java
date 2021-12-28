package kitchenpos.table.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.table.domain.TableGroup;

public class TableGroupResponse {

	private Long id;

	private LocalDateTime createdData;

	private List<TableResponse> orderTables;

	public TableGroupResponse() {
	}

	public TableGroupResponse(Long id, LocalDateTime createdData,
		List<TableResponse> orderTables) {
		this.id = id;
		this.createdData = createdData;
		this.orderTables = orderTables;
	}

	public TableGroupResponse(TableGroup tableGroup) {
		this.id = tableGroup.getId();
		this.createdData = tableGroup.getCreatedDate();
		this.orderTables = tableGroup.getOrderTables().toList().stream()
			.map(TableResponse::new)
			.collect(Collectors.toList());
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getCreatedData() {
		return createdData;
	}

	public List<TableResponse> getOrderTables() {
		return orderTables;
	}
}

package kitchenpos.tableGroup.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.tableGroup.domain.TableGroup;

public class TableGroupResponse {
	private Long id;
	private LocalDateTime createdDate;
	@JsonIgnore
	private List<OrderTableResponse> orderTables;

	public TableGroupResponse() {
	}

	public TableGroupResponse(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
		this.id = id;
		this.createdDate = createdDate;
		this.orderTables = orderTables.stream()
			.map(orderTable -> OrderTableResponse.of(orderTable))
			.collect(Collectors.toList());
	}

	public static TableGroupResponse of(TableGroup tableGroup) {
		return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(), tableGroup.getOrderTables());
	}

	public Long getId() {
		return id;
	}

	public List<OrderTableResponse> getOrderTables() {
		return orderTables;
	}
}

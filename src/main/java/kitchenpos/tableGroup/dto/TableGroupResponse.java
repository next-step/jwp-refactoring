package kitchenpos.tableGroup.dto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.tableGroup.domain.TableGroup;

public class TableGroupResponse {
	private Long id;
	private LocalDateTime createdDate;
	private List<OrderTable> orderTables;

	public TableGroupResponse() {
	}

	public TableGroupResponse(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
		this.id = id;
		this.createdDate = createdDate;
		this.orderTables = orderTables;
	}

	public static TableGroupResponse of(TableGroup tableGroup) {
		return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(), tableGroup.getOrderTables());
	}

	public Long getId() {
		return id;
	}

	public Collection<Object> getOrderTables() {
		return null;
	}
}

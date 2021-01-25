package kitchenpos.table.dto;

import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.table.domain.TableGroup;

public class TableGroupReponse {
	private Long id;
	private LocalDateTime createdDate;
	private List<OrderTableResponse> orderTables;

	public TableGroupReponse() {
	}

	public TableGroupReponse(Long id, LocalDateTime createdDate,
		List<OrderTableResponse> orderTables) {
		this.id = id;
		this.createdDate = createdDate;
		this.orderTables = orderTables;
	}

	public static TableGroupReponse from(TableGroup tableGroup) {
		return new TableGroupReponse(
			tableGroup.getId(),
			tableGroup.getCreatedDate(),
			OrderTableResponse.newList(tableGroup.getOrderTables())
		);
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

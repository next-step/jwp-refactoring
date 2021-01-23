package kitchenpos.tablegroup.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.ordertable.domain.OrderTables;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.tablegroup.domain.TableGroup;

public class TableGroupResponse {

	private final Long id;
	private final LocalDateTime createdDate;
	private final List<OrderTableResponse> orderTables;

	private TableGroupResponse(final Long id, final LocalDateTime createdDate,
		final List<OrderTableResponse> orderTables) {
		this.id = id;
		this.createdDate = createdDate;
		this.orderTables = orderTables;
	}

	public static TableGroupResponse of(final Long id, final LocalDateTime createdDate,
		final List<OrderTableResponse> orderTables) {
		return new TableGroupResponse(id, createdDate, orderTables);
	}

	public static TableGroupResponse of(TableGroup tableGroup, OrderTables orderTables) {
		List<OrderTableResponse> orderTableResponses = orderTables.stream()
			.map(OrderTableResponse::of)
			.collect(Collectors.toList());
		return of(tableGroup.getId(), tableGroup.getCreatedDate(), orderTableResponses);
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

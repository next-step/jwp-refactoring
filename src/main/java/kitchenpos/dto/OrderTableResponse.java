package kitchenpos.dto;

import static java.util.Objects.*;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class OrderTableResponse {

	private Long id;

	private Long tableGroupId;

	private boolean empty;

	public OrderTableResponse(Long id, boolean empty) {
		this(id, null, empty);
	}

	public OrderTableResponse(Long id, Long tableGroupId, boolean empty) {
		this.id = id;
		this.tableGroupId = tableGroupId;
		this.empty = empty;
	}

	public static OrderTableResponse of(OrderTable orderTable) {
		TableGroup tableGroup = orderTable.getTableGroup();
		if (isNull(tableGroup)) {
			return new OrderTableResponse(orderTable.getId(), orderTable.isEmpty());
		}
		return new OrderTableResponse(orderTable.getId(), tableGroup.getId(), orderTable.isEmpty());
	}

	public static List<OrderTableResponse> listOf(List<OrderTable> orderTables) {
		return orderTables.stream()
			.map(OrderTableResponse::of)
			.collect(Collectors.toList());
	}

	public Long getId() {
		return id;
	}

	public Long getTableGroupId() {
		return tableGroupId;
	}

	public boolean isEmpty() {
		return empty;
	}
}

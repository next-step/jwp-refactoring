package kitchenpos.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.OrderTable;

public class OrderTableResponse {

	private Long id;

	private Long tableGroupId;

	private boolean empty;

	public OrderTableResponse(Long id, Long tableGroupId, boolean empty) {
		this.id = id;
		this.tableGroupId = tableGroupId;
		this.empty = empty;
	}

	public static OrderTableResponse of(OrderTable orderTable) {
		return new OrderTableResponse(orderTable.getId(), orderTable.getTableId(), orderTable.isEmpty());
	}

	public static List<OrderTableResponse> listOf(List<OrderTable> orderTables) {
		return orderTables.stream()
			.map(OrderTableResponse::of)
			.collect(Collectors.toList());
	}
}

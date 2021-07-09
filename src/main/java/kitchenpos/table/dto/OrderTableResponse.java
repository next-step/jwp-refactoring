package kitchenpos.table.dto;

import static java.util.Objects.*;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.table.domain.OrderTable;

public class OrderTableResponse {

	private Long id;

	private Long tableGroupId;

	private int numberOfGuests;

	private boolean empty;

	public OrderTableResponse(OrderTable orderTable) {
		this.id = orderTable.getId();
		this.tableGroupId = orderTable.getTableGroupId();
		this.numberOfGuests = orderTable.getNumberOfGuests();
		this.empty = orderTable.isEmpty();
	}

	public static OrderTableResponse of(OrderTable orderTable) {
		return new OrderTableResponse(orderTable);
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

	public int getNumberOfGuests() { return numberOfGuests; }

	public boolean isEmpty() {
		return empty;
	}
}

package kitchenpos.order.ui.response;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.order.domain.OrderTable;

public class OrderTableResponse {

	private long id;
	private Long tableGroupId;
	private int numberOfGuests;
	private boolean empty;

	private OrderTableResponse() {
	}

	private OrderTableResponse(long id, Long tableGroupId, int numberOfGuests, boolean empty) {
		this.id = id;
		this.tableGroupId = tableGroupId;
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;

	}

	public static OrderTableResponse of(long id, Long tableGroupId, int numberOfGuests, boolean empty) {
		return new OrderTableResponse(id, tableGroupId, numberOfGuests, empty);
	}

	public static List<OrderTableResponse> listOf(List<OrderTable> orderTables) {
		return orderTables.stream()
			.map(orderTable -> OrderTableResponse.of(orderTable.getId(), orderTable.getTableGroupId(),
				orderTable.getNumberOfGuests(), orderTable.isEmpty()))
			.collect(Collectors.toList());
	}

	public static OrderTableResponse from(OrderTable orderTable) {
		return OrderTableResponse.of(orderTable.getId(), orderTable.getTableGroupId(),
			orderTable.getNumberOfGuests(), orderTable.isEmpty());
	}

	public static List<OrderTableResponse> listFrom(List<OrderTable> orderTables) {
		return orderTables.stream()
			.map(OrderTableResponse::from)
			.collect(Collectors.toList());
	}

	public long getId() {
		return id;
	}

	public Long getTableGroupId() {
		return tableGroupId;
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}

	public boolean isEmpty() {
		return empty;
	}
}

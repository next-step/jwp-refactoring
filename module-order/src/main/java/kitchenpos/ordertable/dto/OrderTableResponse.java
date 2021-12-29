package kitchenpos.ordertable.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.ordertable.domain.OrderTable;

public class OrderTableResponse {

	private Long id;
	private Long tableGroupId;
	private int numberOfGuests;
	private boolean empty;

	public OrderTableResponse() {
	}

	public OrderTableResponse(long id, long tableGroupId, int numberOfGuests, boolean empty) {
		this.id = id;
		this.tableGroupId = tableGroupId;
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
	}

	public static OrderTableResponse of(OrderTable orderTable) {
		return new OrderTableResponse(orderTable.getId(), orderTable.getTableGroupId(), orderTable.getNumberOfGuests(),
			orderTable.getEmpty());
	}

	public static List<OrderTableResponse> ofList(List<OrderTable> orderTables) {
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

	public int getNumberOfGuests() {
		return numberOfGuests;
	}

	public boolean isEmpty() {
		return empty;
	}
}

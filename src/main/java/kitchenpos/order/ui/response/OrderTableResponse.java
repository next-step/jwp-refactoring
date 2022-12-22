package kitchenpos.order.ui.response;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;

public class OrderTableResponse {

	private long id;
	private Long tableGroupId;
	private int numberOfGuests;
	private boolean empty;

	private OrderTableResponse() {
	}

	private OrderTableResponse(long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
		this.id = id;
		if (tableGroup != null) {
			this.tableGroupId = tableGroup.id();
		}
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
	}

	public static OrderTableResponse from(OrderTable orderTable) {
		return new OrderTableResponse(
			orderTable.id(),
			orderTable.tableGroup(),
			orderTable.numberOfGuests().value(),
			orderTable.isEmpty()
		);
	}

	public static OrderTableResponse of(long id, int numberOfGuests, boolean empty) {
		return new OrderTableResponse(id, null, numberOfGuests, empty);
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

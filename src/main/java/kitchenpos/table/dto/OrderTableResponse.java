package kitchenpos.table.dto;

import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tableGroup.dto.TableGroupResponse;

public class OrderTableResponse {
	private Long id;
	private TableGroupResponse tableGroup;
	private NumberOfGuests numberOfGuest;
	private boolean empty;

	public OrderTableResponse() {
	}

	public Long getId() {
		return id;
	}

	public OrderTableResponse(Long id, TableGroupResponse tableGroup, NumberOfGuests numberOfGuest, boolean empty) {
		this.id = id;
		this.tableGroup = tableGroup;
		this.numberOfGuest = numberOfGuest;
		this.empty = empty;
	}

	public static OrderTableResponse of(OrderTable orderTable) {
		return new OrderTableResponse(orderTable.getId(), TableGroupResponse.of(orderTable.getTableGroup()), orderTable.getNumberOfGuest(), orderTable.isEmpty());
	}
}

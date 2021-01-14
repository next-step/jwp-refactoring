package kitchenpos.dto;

import kitchenpos.domain.OrderTable;

public class OrderTableResponse {
	private long id;
	private Long tableGroupId;
	private int numberOfGuests;
	private boolean empty;

	public OrderTableResponse() {
	}

	public static OrderTableResponse of(OrderTable orderTable) {
		return new OrderTableResponse(orderTable.getId(), orderTable.getTableGroupId(),
				orderTable.getNumberOfGuests(), orderTable.isEmpty());
	}

	public OrderTableResponse(long id, Long tableGroupId, int numberOfGuests, boolean empty) {
		this.id = id;
		this.tableGroupId = tableGroupId;
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
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

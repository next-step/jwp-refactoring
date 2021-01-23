package kitchenpos.ordertable.dto;

import kitchenpos.ordertable.domain.OrderTable;

public class OrderTableResponse {
	private Long id;
	private int numberOfGuests;
	private boolean empty;

	public OrderTableResponse() {
	}

	public OrderTableResponse(final Long id, final int numberOfGuests, final boolean empty) {
		this.id = id;
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
	}

	public static OrderTableResponse of(final OrderTable orderTable) {
		return new OrderTableResponse(orderTable.getId(), orderTable.numberOfGuests(), orderTable.isEmpty());
	}

	public Long getId() {
		return id;
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}

	public boolean isEmpty() {
		return empty;
	}
}

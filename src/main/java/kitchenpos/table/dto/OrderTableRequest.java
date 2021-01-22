package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;

public class OrderTableRequest {
	private int numberOfGuests;
	private boolean empty;

	public OrderTableRequest() {
	}

	public OrderTableRequest(final int numberOfGuests, final boolean empty) {
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
	}

	public static OrderTableRequest of(final int numberOfGuests, final boolean empty) {
		return new OrderTableRequest(numberOfGuests, empty);
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}

	public boolean isEmpty() {
		return empty;
	}

	public OrderTable toEntity() {
		return OrderTable.of(numberOfGuests, empty);
	}
}

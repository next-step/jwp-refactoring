package kitchenpos.dto;

import kitchenpos.domain.OrderTable;

public class OrderTableRequest {
	private int numberOfGuests;
	private boolean empty;

	public OrderTableRequest() {
	}

	public OrderTableRequest(int numberOfGuests) {
		this.numberOfGuests = numberOfGuests;
	}

	public OrderTableRequest(boolean empty) {
		this.empty = empty;
	}

	public OrderTableRequest(Integer numberOfGuests, Boolean empty) {
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}

	public boolean isEmpty() {
		return empty;
	}

	public OrderTable toOrderTable() {
		return new OrderTable(this.numberOfGuests, this.empty);
	}
}

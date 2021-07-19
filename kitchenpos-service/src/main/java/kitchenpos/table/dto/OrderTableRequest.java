package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;

import java.util.Objects;

public class OrderTableRequest {
	private int numberOfGuests;
	private boolean empty;

	public OrderTableRequest() {
	}

	public OrderTableRequest(int numberOfGuests, boolean empty) {
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		OrderTableRequest that = (OrderTableRequest) o;
		return numberOfGuests == that.numberOfGuests && empty == that.empty;
	}

	@Override
	public int hashCode() {
		return Objects.hash(numberOfGuests, empty);
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}

	public boolean isEmpty() {
		return empty;
	}

	public OrderTable toOrderTable() {
		return new OrderTable(numberOfGuests, empty);
	}
}

package kitchenpos.ordertable.dto;

import kitchenpos.domain.OrderTable;
import kitchenpos.ordertable.domain.NumberOfGuests;

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

	public OrderTableRequest(int numberOfGuests, boolean empty) {
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}

	public void setNumberOfGuests(int numberOfGuests) {
		this.numberOfGuests = numberOfGuests;
	}

	public boolean isEmpty() {
		return empty;
	}

	public void setEmpty(boolean empty) {
		this.empty = empty;
	}

	public OrderTable toOrderTable() {
		OrderTable orderTable = new OrderTable();
		orderTable.setNumberOfGuests(numberOfGuests);
		orderTable.setEmpty(empty);
		return orderTable;
	}

	public kitchenpos.ordertable.domain.OrderTable toToBeOrderTable() {
		return kitchenpos.ordertable.domain.OrderTable.of(NumberOfGuests.of(numberOfGuests), empty);
	}
}

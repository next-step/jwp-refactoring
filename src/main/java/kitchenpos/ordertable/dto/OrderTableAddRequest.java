package kitchenpos.ordertable.dto;

import kitchenpos.ordertable.domain.OrderTable;

public class OrderTableAddRequest {

	private int numberOfGuests;
	private boolean empty;

	protected OrderTableAddRequest() {
	}

	private OrderTableAddRequest(int numberOfGuests, boolean empty) {
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
	}

	public static OrderTableAddRequest of(int numberOfGuests, boolean empty) {
		return new OrderTableAddRequest(numberOfGuests, empty);
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

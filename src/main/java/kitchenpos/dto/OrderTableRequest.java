package kitchenpos.dto;

public class OrderTableRequest {

	private int numberOfGuests;
	private boolean empty;

	private OrderTableRequest() {
	}

	private OrderTableRequest(boolean empty) {
		this.empty = empty;
	}

	public OrderTableRequest(int numberOfGuests) {
		this.numberOfGuests = numberOfGuests;
	}

	public static OrderTableRequest of(boolean empty) {
		return new OrderTableRequest(empty);
	}

	public static OrderTableRequest of(int numberOfGuests) {
		return new OrderTableRequest(numberOfGuests);
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}

	public boolean isEmpty() {
		return empty;
	}
}

package kitchenpos.ordertable.dto;

public class OrderTableNumberOfGuestsRequest {

	private int numberOfGuests;

	protected OrderTableNumberOfGuestsRequest() {
	}

	private OrderTableNumberOfGuestsRequest(int numberOfGuests) {
		this.numberOfGuests = numberOfGuests;
	}

	public static OrderTableNumberOfGuestsRequest of(int numberOfGuests) {
		return new OrderTableNumberOfGuestsRequest(numberOfGuests);
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}
}

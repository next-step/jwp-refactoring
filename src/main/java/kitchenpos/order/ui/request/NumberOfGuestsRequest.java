package kitchenpos.order.ui.request;

public class NumberOfGuestsRequest {
	private final int numberOfGuests;

	public NumberOfGuestsRequest(int numberOfGuests) {
		this.numberOfGuests = numberOfGuests;
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}
}

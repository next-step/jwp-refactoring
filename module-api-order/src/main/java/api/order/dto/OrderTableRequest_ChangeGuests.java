package api.order.dto;

public class OrderTableRequest_ChangeGuests {
	private int numberOfGuests;

	public OrderTableRequest_ChangeGuests() {
	}

	public OrderTableRequest_ChangeGuests(int numberOfGuests) {
		this.numberOfGuests = numberOfGuests;
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}
}

package kitchenpos.table.dto;

public class TableGuestsUpdateRequest {

	private int numberOfGuests;

	public TableGuestsUpdateRequest() {
	}

	public TableGuestsUpdateRequest(int numberOfGuests) {
		this.numberOfGuests = numberOfGuests;
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}
}

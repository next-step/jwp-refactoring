package kitchenpos.table.table.ui.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NumberOfGuestsRequest {
	private final int numberOfGuests;

	@JsonCreator
	public NumberOfGuestsRequest(@JsonProperty("numberOfGuests") int numberOfGuests) {
		this.numberOfGuests = numberOfGuests;
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}
}

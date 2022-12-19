package kitchenpos.ordertable.dto;

import kitchenpos.ordertable.domain.NumberOfGuests;

public class UpdateNumberOfGuestsRequest {
    private int numberOfGuests;

    protected UpdateNumberOfGuestsRequest() {}

    private UpdateNumberOfGuestsRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public static UpdateNumberOfGuestsRequest of(int numberOfGuests) {
        return new UpdateNumberOfGuestsRequest(numberOfGuests);
    }

    public NumberOfGuests getNumberOfGuests() {
        return new NumberOfGuests(numberOfGuests);
    }
}

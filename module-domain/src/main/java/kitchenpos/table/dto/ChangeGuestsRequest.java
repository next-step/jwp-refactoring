package kitchenpos.table.dto;

import kitchenpos.table.domain.NumberOfGuests;

public class ChangeGuestsRequest {
    private int numberOfGuests;

    protected ChangeGuestsRequest() {
    }

    public ChangeGuestsRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public NumberOfGuests toNumberOfGuests() {
        return new NumberOfGuests(numberOfGuests);
    }
}

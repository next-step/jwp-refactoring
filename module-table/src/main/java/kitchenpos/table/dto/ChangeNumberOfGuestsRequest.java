package kitchenpos.table.dto;

import kitchenpos.table.domain.NumberOfGuests;

public class ChangeNumberOfGuestsRequest {

    private int numberOfGuests;

    public ChangeNumberOfGuestsRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public NumberOfGuests getNumberOfGuests() {
        return new NumberOfGuests(this.numberOfGuests);
    }
}

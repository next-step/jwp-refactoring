package kitchenpos.dto;

import javax.validation.constraints.NotNull;

public class NumberOfGuestsRequest {
    @NotNull
    private int numberOfGuests;

    protected NumberOfGuestsRequest() {}

    public NumberOfGuestsRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}

package kitchenpos.table.dto;

public class NumberOfGuestsRequest {
    private int numberOfGuests;

    public NumberOfGuestsRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    protected NumberOfGuestsRequest() {
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}

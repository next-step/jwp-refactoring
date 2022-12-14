package kitchenpos.ordertable.dto;

public class UpdateNumberOfGuestsRequest {
    private int numberOfGuests;

    protected UpdateNumberOfGuestsRequest() {}

    public UpdateNumberOfGuestsRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public static UpdateNumberOfGuestsRequest of(int numberOfGuests) {
        return new UpdateNumberOfGuestsRequest(numberOfGuests);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}

package kitchenpos.table.dto;

public class ChangeNumberOfGuestsRequest {
    private int numberOfGuests;

    protected ChangeNumberOfGuestsRequest() {
    }

    public ChangeNumberOfGuestsRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public static ChangeNumberOfGuestsRequest of(int numberOfGuests) {
        return new ChangeNumberOfGuestsRequest(numberOfGuests);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}

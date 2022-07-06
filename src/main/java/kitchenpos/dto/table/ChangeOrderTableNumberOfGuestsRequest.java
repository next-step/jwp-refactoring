package kitchenpos.dto.table;

public class ChangeOrderTableNumberOfGuestsRequest {

    private int numberOfGuests;

    public ChangeOrderTableNumberOfGuestsRequest() {

    }

    public ChangeOrderTableNumberOfGuestsRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}

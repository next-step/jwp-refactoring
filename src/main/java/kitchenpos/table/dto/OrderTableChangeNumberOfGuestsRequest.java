package kitchenpos.table.dto;

public class OrderTableChangeNumberOfGuestsRequest {

    private int numberOfGuests;

    public OrderTableChangeNumberOfGuestsRequest() {
        // empty
    }

    public OrderTableChangeNumberOfGuestsRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}

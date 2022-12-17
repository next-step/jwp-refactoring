package kitchenpos.dto.request;

public class OrderTableNumberOfGuestsRequest {
    private int numberOfGuests;

    public OrderTableNumberOfGuestsRequest() {
    }

    public OrderTableNumberOfGuestsRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}

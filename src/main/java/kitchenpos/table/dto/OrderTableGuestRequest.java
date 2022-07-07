package kitchenpos.table.dto;

public class OrderTableGuestRequest {
    private int numberOfGuests;

    private OrderTableGuestRequest() {
    }

    public OrderTableGuestRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}

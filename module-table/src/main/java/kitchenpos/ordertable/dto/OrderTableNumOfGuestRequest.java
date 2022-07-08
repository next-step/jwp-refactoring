package kitchenpos.ordertable.dto;

public class OrderTableNumOfGuestRequest {
    int numberOfGuests;

    protected OrderTableNumOfGuestRequest() {
    }

    private OrderTableNumOfGuestRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public static OrderTableNumOfGuestRequest from(int numberOfGuests) {
        return new OrderTableNumOfGuestRequest(numberOfGuests);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}

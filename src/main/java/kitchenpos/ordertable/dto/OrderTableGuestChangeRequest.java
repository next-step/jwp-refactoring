package kitchenpos.ordertable.dto;

public class OrderTableGuestChangeRequest {
    private Integer numberOfGuests;

    public OrderTableGuestChangeRequest() {
    }

    public OrderTableGuestChangeRequest(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}

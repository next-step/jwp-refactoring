package kitchenpos.ordertable.dto;

public class OrderTableRequest {
    private Integer numberOfGuests;
    private boolean empty;

    public OrderTableRequest() {
    }

    public OrderTableRequest(Integer numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}

package kitchenpos.dto;

public class OrderTableRequest {

    private int numberOfGuests;
    private boolean empty;

    public OrderTableRequest() {

    }
    public OrderTableRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public OrderTableRequest(int numberOfGuests, boolean empty) {
        this(numberOfGuests);
        this.empty = empty;
    }


    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}

package kitchenpos.dto;

public class OrderTableResponse {
    Long id;
    int numberOfGuests;
    boolean empty;

    protected OrderTableResponse() {
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}

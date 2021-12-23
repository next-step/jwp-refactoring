package kitchenpos.dto;

public class OrderTableRequest {
    private int numberOfGuests;
    private boolean empty;

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}

package kitchenpos.order.dto;

public class TableRequest {
    private int numberOfGuests;
    private boolean empty;

    public TableRequest(int numberOfGuests, boolean isEmpty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = isEmpty;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void makeOrderTable() {

    }
}

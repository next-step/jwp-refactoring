package kitchenpos.table.dto;

public class TableRequest {
    private int numberOfGuests;
    private boolean empty;

    protected TableRequest() {}

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

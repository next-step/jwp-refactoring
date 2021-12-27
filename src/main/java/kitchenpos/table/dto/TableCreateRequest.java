package kitchenpos.table.dto;

public class TableCreateRequest {

    private int numberOfGuest;
    private boolean empty;

    public TableCreateRequest() {
    }

    public TableCreateRequest(final int numberOfGuest, final boolean empty) {
        this.numberOfGuest = numberOfGuest;
        this.empty = empty;
    }

    public int getNumberOfGuest() {
        return numberOfGuest;
    }

    public boolean isEmpty() {
        return empty;
    }
}
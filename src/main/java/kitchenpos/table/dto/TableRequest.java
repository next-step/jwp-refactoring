package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;

public class TableRequest {
    private int numberOfGuests;
    private boolean empty;

    public TableRequest() {
    }

    private TableRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static TableRequest of(int numberOfGuests, boolean empty) {
        return new TableRequest(numberOfGuests, empty);
    }

    public OrderTable toOrderTable() {
        return OrderTable.of(numberOfGuests, empty);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}

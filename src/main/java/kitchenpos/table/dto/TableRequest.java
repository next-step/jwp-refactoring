package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;

public class TableRequest {

    private final int numberOfGuests;
    private final boolean empty;

    public TableRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static TableRequest from(OrderTable orderTable) {
        return new TableRequest(orderTable.getNumberOfGuests(), orderTable.isEmpty());
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public OrderTable toEntity() {
        return new OrderTable(numberOfGuests, empty);
    }
}

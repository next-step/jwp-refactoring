package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;

public class OrderTableRequest {
    private int numberOfGuests;
    private boolean empty;

    protected OrderTableRequest() {}

    public OrderTableRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public static OrderTable of(OrderTableRequest orderTable) {
        return new OrderTable(orderTable.getNumberOfGuests(), orderTable.isEmpty());
    }
}

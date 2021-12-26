package kitchenpos.table.dto;

import kitchenpos.table.domain.Empty;
import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;

public class OrderTableRequest {

    private int numberOfGuests;
    private boolean empty;

    protected OrderTableRequest() {
    }

    private OrderTableRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableRequest of(int numberOfGuests, boolean empty) {
        return new OrderTableRequest(numberOfGuests, empty);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public OrderTable toOrderTable() {
        return OrderTable.of(NumberOfGuests.of(numberOfGuests), Empty.of(empty));
    }
}

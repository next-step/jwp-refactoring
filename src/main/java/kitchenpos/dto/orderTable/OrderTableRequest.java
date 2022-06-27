package kitchenpos.dto.orderTable;

import kitchenpos.domain.orderTable.OrderTable;

public class OrderTableRequest {
    private final int numberOfGuests;
    private final boolean empty;

    public OrderTableRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable toOrderTable() {
        return new OrderTable(null, null, numberOfGuests, empty);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}

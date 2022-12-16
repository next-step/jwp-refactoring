package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;

public class OrderTableRequest {
    private final int numberOfGuests;
    private final boolean empty;

    public OrderTableRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable toEntity() {
        return OrderTable.of(numberOfGuests, empty);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}

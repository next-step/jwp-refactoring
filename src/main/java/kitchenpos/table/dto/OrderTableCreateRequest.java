package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;

public class OrderTableCreateRequest {

    private final Integer numberOfGuests;
    private final boolean empty;

    public OrderTableCreateRequest(Integer numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public OrderTable toOrderTable() {
        return OrderTable.of(numberOfGuests, empty);
    }
}

package kitchenpos.dto;

import kitchenpos.domain.OrderTable;

public class TableRequest {

    private int numberOfGuests;

    private boolean empty;

    public OrderTable toEntity() {
        return new OrderTable(null, numberOfGuests, empty);
    }

    public TableRequest() {
    }

    public TableRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}

package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;

public class OrderTableRequest {
    private long id;
    private int numberOfGuests;
    private boolean empty;

    public OrderTableRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public OrderTableRequest(boolean empty) {
        this.empty = empty;
    }

    public OrderTableRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public OrderTable toTable() {
        return new OrderTable(numberOfGuests, empty);
    }
}

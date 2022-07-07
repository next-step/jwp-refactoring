package kichenpos.table.ui.dto;

import kichenpos.table.domain.OrderTable;

public class OrderTableUpdateRequest {
    private int numberOfGuests;
    private boolean empty;

    private OrderTableUpdateRequest() {
    }

    public OrderTableUpdateRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable toEntity() {
        return new OrderTable(numberOfGuests, empty);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}

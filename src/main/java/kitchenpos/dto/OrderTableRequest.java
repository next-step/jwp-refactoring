package kitchenpos.dto;

import kitchenpos.domain.OrderTableEntity;

public class OrderTableRequest {
    private int numberOfGuests;
    private boolean empty;

    public OrderTableRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTableRequest() {
    }

    public OrderTableEntity toOrderTable() {
        return new OrderTableEntity(numberOfGuests, empty);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}

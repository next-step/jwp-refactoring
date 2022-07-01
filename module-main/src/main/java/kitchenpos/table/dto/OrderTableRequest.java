package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;

public class OrderTableRequest {

    private boolean empty;

    private int numberOfGuests;

    protected OrderTableRequest() {
    }

    private OrderTableRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    private OrderTableRequest(boolean empty) {
        this.empty = empty;
    }

    private OrderTableRequest(boolean empty, int numberOfGuests) {
        this.empty = empty;
        this.numberOfGuests = numberOfGuests;
    }

    public static OrderTableRequest of(int numberOfGuests) {
        return new OrderTableRequest(numberOfGuests);
    }

    public static OrderTableRequest of(boolean empty) {
        return new OrderTableRequest(empty);
    }

    public static OrderTableRequest of(boolean empty, int numberOfGuests) {
        return new OrderTableRequest(empty, numberOfGuests);
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

package kitchenpos.orders.table.dto;

import kitchenpos.orders.table.domain.OrderTable;

public class OrderTableRequest {

    private int numberOfGuests;
    private boolean empty;

    protected OrderTableRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    protected OrderTableRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    protected OrderTableRequest(boolean empty) {
        this.empty = empty;
    }

    protected OrderTableRequest() {
    }

    public static OrderTableRequest of(int numberOfGuests, boolean empty) {
        return new OrderTableRequest(numberOfGuests, empty);
    }

    public static OrderTableRequest of(int numberOfGuests) {
        return new OrderTableRequest(numberOfGuests);
    }

    public static OrderTableRequest of(boolean empty) {
        return new OrderTableRequest(empty);
    }

    public OrderTable toOrderTable() {
        return new OrderTable(numberOfGuests, empty);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}

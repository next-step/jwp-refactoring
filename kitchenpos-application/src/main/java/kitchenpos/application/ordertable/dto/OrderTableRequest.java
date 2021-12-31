package kitchenpos.application.ordertable.dto;

import kitchenpos.ordertable.domain.OrderTable;

public class OrderTableRequest {
    private int numberOfGuests;
    private boolean empty;

    public OrderTableRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    private OrderTableRequest(boolean empty) {
        this.empty = empty;
    }

    private OrderTableRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public static OrderTableRequest of(int numberOfGuests, boolean empty) {
        return new OrderTableRequest(numberOfGuests, empty);
    }

    public static OrderTableRequest makeChangeNumberOfGuestsRequest(int numberOfGuests) {
        return new OrderTableRequest(numberOfGuests);
    }

    public static OrderTableRequest makeChangeEmptyRequest(boolean empty) {
        return new OrderTableRequest(empty);
    }

    public OrderTable toEntity() {
        return OrderTable.of(numberOfGuests, empty);
    }

    public boolean isEmpty() {
        return empty;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}

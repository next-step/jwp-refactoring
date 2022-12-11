package kitchenpos.ordertable.dto;


import kitchenpos.ordertable.domain.OrderTable;

public class TableRequest {
    private int numberOfGuests;
    private boolean empty;

    public TableRequest() {
    }

    public TableRequest(int numberOfGuests, boolean empty) {
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

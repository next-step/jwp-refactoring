package kitchenpos.dto.table;

import kitchenpos.domain.table.OrderTable;

public class CreateOrderTableRequest {

    private int numberOfGuests;
    private boolean empty;

    public CreateOrderTableRequest() {

    }

    public CreateOrderTableRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
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

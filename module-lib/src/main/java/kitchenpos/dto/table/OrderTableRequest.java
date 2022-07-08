package kitchenpos.dto.table;

import kitchenpos.domain.table.OrderTable;

public class OrderTableRequest {
    private int numberOfGuests;
    private boolean empty;

    public OrderTableRequest() {
    }

    public OrderTableRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable toOrderTable(OrderTableRequest orderTableRequest) {
        return new OrderTable(orderTableRequest.getNumberOfGuests(), orderTableRequest.empty);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}

package kitchenpos.common.table.dto;

import kitchenpos.common.table.domain.NumberOfGuests;
import kitchenpos.common.table.domain.OrderTable;

public class OrderTableRequest {
    private NumberOfGuests numberOfGuests;

    private boolean empty;

    public OrderTableRequest() {
    }

    public OrderTableRequest(boolean empty) {
        this.empty = empty;
    }

    public OrderTableRequest(NumberOfGuests numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public OrderTableRequest(final NumberOfGuests numberOfGuests, final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public NumberOfGuests getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public OrderTable toOrderTable() {
        return new OrderTable(numberOfGuests, empty);
    }
}

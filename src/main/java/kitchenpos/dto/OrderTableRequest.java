package kitchenpos.dto;

import kitchenpos.domain.NumberOfGuests;
import kitchenpos.domain.OrderTable;

public class OrderTableRequest {
    private int numberOfGuests;
    private boolean empty;

    protected OrderTableRequest() {
    }
    
    public OrderTableRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable toOrderTable() {
        return new OrderTable(NumberOfGuests.from(numberOfGuests), empty);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}

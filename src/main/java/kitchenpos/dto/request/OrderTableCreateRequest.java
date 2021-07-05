package kitchenpos.dto.request;


import kitchenpos.domain.NumberOfGuest;
import kitchenpos.domain.table.OrderTableCreate;

public class OrderTableCreateRequest {
    private int numberOfGuests;
    private boolean empty;

    protected OrderTableCreateRequest() {
    }

    public OrderTableCreateRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTableCreate toCreate() {
        return new OrderTableCreate(new NumberOfGuest(numberOfGuests), empty);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}

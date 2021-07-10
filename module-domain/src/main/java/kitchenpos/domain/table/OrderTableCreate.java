package kitchenpos.domain.table;

import kitchenpos.domain.NumberOfGuest;

public class OrderTableCreate {
    private NumberOfGuest numberOfGuests;
    private boolean empty;

    public OrderTableCreate(NumberOfGuest numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public NumberOfGuest getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}

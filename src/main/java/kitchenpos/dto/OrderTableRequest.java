package kitchenpos.dto;

import kitchenpos.domain.NumberOfGuests;

public class OrderTableRequest {
    private NumberOfGuests numberOfGuests;

    private boolean empty;

    public OrderTableRequest() {
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
}

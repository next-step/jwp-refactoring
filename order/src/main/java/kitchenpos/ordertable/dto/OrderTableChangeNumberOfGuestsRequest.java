package kitchenpos.ordertable.dto;

import kitchenpos.ordertable.domain.NumberOfGuests;

public class OrderTableChangeNumberOfGuestsRequest {

    private final int numberOfGuests;

    public OrderTableChangeNumberOfGuestsRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public NumberOfGuests getNumberOfGuests() {
        return new NumberOfGuests(numberOfGuests);
    }
}

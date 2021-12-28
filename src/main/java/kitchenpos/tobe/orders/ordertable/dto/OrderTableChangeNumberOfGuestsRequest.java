package kitchenpos.tobe.orders.ordertable.dto;

import kitchenpos.tobe.orders.ordertable.domain.NumberOfGuests;

public class OrderTableChangeNumberOfGuestsRequest {

    private final int numberOfGuests;

    public OrderTableChangeNumberOfGuestsRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public NumberOfGuests getNumberOfGuests() {
        return new NumberOfGuests(numberOfGuests);
    }
}

package kitchenpos.tobe.orders.dto.ordertable;

import kitchenpos.tobe.orders.domain.ordertable.NumberOfGuests;

public class OrderTableChangeNumberOfGuestsRequest {

    private final int numberOfGuests;

    public OrderTableChangeNumberOfGuestsRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public NumberOfGuests getNumberOfGuests() {
        return new NumberOfGuests(numberOfGuests);
    }
}

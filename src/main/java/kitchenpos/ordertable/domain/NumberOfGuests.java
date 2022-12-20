package kitchenpos.ordertable.domain;

import kitchenpos.common.error.ErrorEnum;

public class NumberOfGuests {
    private int numberOfGuests;

    protected NumberOfGuests() {}

    public NumberOfGuests(int numberOfGuests) {
        validate(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validate(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException(ErrorEnum.GUESTS_UNDER_ZERO.message());
        }
    }

    public int value() {
        return numberOfGuests;
    }
}

package kitchenpos.ordertable.domain;

import kitchenpos.common.error.ErrorEnum;

public class NumberOfGuests {
    private static final int ZERO = 0;

    private int numberOfGuests;

    protected NumberOfGuests() {}

    public NumberOfGuests(int numberOfGuests) {
        validate(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validate(int numberOfGuests) {
        if (numberOfGuests < ZERO) {
            throw new IllegalArgumentException(ErrorEnum.GUESTS_UNDER_ZERO.message());
        }
    }

    public int value() {
        return numberOfGuests;
    }
}

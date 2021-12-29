package kitchenpos.order.domain;

import kitchenpos.common.exception.InputDataErrorCode;
import kitchenpos.common.exception.InputDataException;

public class NumberOfGuests {
    private static final int MIN_COUNT_OF_GUEST = 0;
    private int numberOfGuests;

    public NumberOfGuests(int numberOfGuests) {
        validate(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    private void validate(int numberOfGuests) {
        if (numberOfGuests < MIN_COUNT_OF_GUEST) {
            throw new InputDataException(InputDataErrorCode.THE_NUMBER_OF_GUESTS_IS_LESS_THAN_ZERO);
        }
    }
}

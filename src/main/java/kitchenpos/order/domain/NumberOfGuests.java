package kitchenpos.order.domain;

import kitchenpos.common.exception.InputDataErrorCode;
import kitchenpos.common.exception.InputDataException;

public class NumberOfGuests {

    private int numberOfGuests;

    public NumberOfGuests(int numberOfGuests) {
        validate(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    private void validate(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new InputDataException(InputDataErrorCode.THE_NUMBER_OF_GUESTS_IS_LESS_THAN_ZERO);
        }
    }
}

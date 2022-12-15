package kitchenpos.order.domain;

import kitchenpos.common.constant.ErrorCode;

import javax.persistence.*;

@Embeddable
public class NumberOfGuests {
    private int numberOfGuests;

    protected NumberOfGuests() {}

    public NumberOfGuests(int numberOfGuests) {
        validate(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validate(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException(ErrorCode.NUMBER_OF_GUESTS_MINIMUM.getMessage());
        }
    }

    public int value() {
        return numberOfGuests;
    }
}

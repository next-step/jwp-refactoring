package kitchenpos.common.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.common.constant.ErrorCode;

@Embeddable
public class NumberOfGuests {

    private static final int ZERO = 0;

    @Column(nullable = false)
    private int numberOfGuests;

    protected NumberOfGuests() {}

    private NumberOfGuests(int numberOfGuests) {
        validateNumberOfGuestsLessThanZero(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public static NumberOfGuests from(int numberOfGuests) {
        return new NumberOfGuests(numberOfGuests);
    }

    private void validateNumberOfGuestsLessThanZero(int numberOfGuests) {
        if (numberOfGuests < ZERO) {
            throw new IllegalArgumentException(ErrorCode.NUMBER_OF_GUESTS_LESS_THAN_ZERO.getErrorMessage());
        }
    }

    public int value() {
        return numberOfGuests;
    }
}

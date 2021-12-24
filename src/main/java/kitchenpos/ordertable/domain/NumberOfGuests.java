package kitchenpos.ordertable.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.common.exception.CommonErrorCode;
import kitchenpos.common.exception.InvalidParameterException;

@Embeddable
public class NumberOfGuests {

    public static final int MIN = 0;

    @Column(name = "numberOfGuests", nullable = false)
    private int numberOfGuests;

    protected NumberOfGuests() {
    }

    private NumberOfGuests(int numberOfGuests) {
        valid(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public static NumberOfGuests of(int numberOfGuests) {
        return new NumberOfGuests(numberOfGuests);
    }

    private void valid(int numberOfGuests) {
        if (numberOfGuests < MIN) {
            throw new InvalidParameterException(
                CommonErrorCode.NUMBER_OF_GUESTS_MIN_UNDER_EXCEPTION);
        }
    }

    public NumberOfGuests changeNumberOfGuests(int changeNumberOfGuests) {
        return of(changeNumberOfGuests);
    }

    public int value() {
        return numberOfGuests;
    }
}

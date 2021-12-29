package kitchenpos.domain;

import kitchenpos.exception.InvalidNumberOfGuestsException;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {
    private static final int NUMBER_OF_GUESTS_MIN_VALUE = 1;
    private static final String INVALID_NUMBER_OF_GUESTS_EXCEPTION = "손님은 최소 1명 이상입니다.";

    @Column(name = "number_of_guests", nullable = false)
    private int numberOfGuests;

    protected NumberOfGuests() {
    }

    private NumberOfGuests(int numberOfGuests) {
        validate(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public static NumberOfGuests of(int numberOfGuests) {
        return new NumberOfGuests(numberOfGuests);
    }

    private static void validate(int numberOfGuests) {
        if (numberOfGuests < NUMBER_OF_GUESTS_MIN_VALUE) {
            throw new InvalidNumberOfGuestsException(INVALID_NUMBER_OF_GUESTS_EXCEPTION);
        }
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}

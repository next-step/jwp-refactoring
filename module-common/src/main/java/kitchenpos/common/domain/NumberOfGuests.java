package kitchenpos.common.domain;

import kitchenpos.common.exception.NegativeNumberOfGuestsException;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {
    public static final int MINIMUM_GUESTS_NUMBER = 0;
    @Column
    private int numberOfGuests;

    protected NumberOfGuests() {
    }

    public NumberOfGuests(int numberOfGuests) {
        validateChangeableNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        validateChangeableNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validateChangeableNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < MINIMUM_GUESTS_NUMBER) {
            throw new NegativeNumberOfGuestsException();
        }
    }
}

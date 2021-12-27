package kitchenpos.common.domain;

import kitchenpos.common.exception.NegativeNumberOfGuestsException;

import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {
    private int numberOfGuests;

    public NumberOfGuests() {
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
        if (numberOfGuests < 0) {
            throw new NegativeNumberOfGuestsException();
        }
    }
}

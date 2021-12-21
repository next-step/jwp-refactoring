package kitchenpos.table.domain;

import kitchenpos.common.exception.GuestsNumberOverException;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {
    private static final int MIN_NUMBER = 0;
    @Column
    private int numberOfGuests;

    protected NumberOfGuests() {
    }

    public NumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
        validateNumber();
    }

    private void validateNumber() {
        if(numberOfGuests < MIN_NUMBER){
            throw new GuestsNumberOverException();
        }
    }

    public int getNumber() {
        return numberOfGuests;
    }
}

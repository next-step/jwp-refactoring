package table.domain;

import javax.persistence.Embeddable;

import kitchenpos.common.exception.KitchenposErrorCode;
import kitchenpos.common.exception.KitchenposException;

@Embeddable
public class NumberOfGuests {
    private static final int MIN_NUMBER = 0;

    private int numberOfGuests;

    protected NumberOfGuests() {
    }

    public NumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < MIN_NUMBER) {
            throw new KitchenposException(KitchenposErrorCode.INVALID_NUMBER_OF_GUESTS);
        }
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}

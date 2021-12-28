package kitchenpos.domain;

import javax.persistence.Embeddable;

import kitchenpos.exception.KitchenposErrorCode;
import kitchenpos.exception.KitchenposException;

@Embeddable
public class NumberOfGuests {
    private int numberOfGuests;

    protected NumberOfGuests() {
    }

    public NumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new KitchenposException(KitchenposErrorCode.INVALID_NUMBER_OF_GUESTS);
        }
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}

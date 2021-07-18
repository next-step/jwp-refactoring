package kitchenpos.table.domain.value;

import javax.persistence.Embeddable;
import kitchenpos.table.exception.NumberOfGuestsNotNegativeNumberException;

@Embeddable
public class NumberOfGuests {

    private int numberOfGuests;

    public NumberOfGuests() {
    }

    private NumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public static NumberOfGuests of(int numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);
        return new NumberOfGuests(numberOfGuests);
    }

    private static void validateNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new NumberOfGuestsNotNegativeNumberException();
        }
    }

    public int getValue() {
        return this.numberOfGuests;
    }
}

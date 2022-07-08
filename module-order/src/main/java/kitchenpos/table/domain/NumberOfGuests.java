package kitchenpos.table.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import static kitchenpos.common.Messages.NUMBER_OF_GUESTS_CANNOT_ZERO;

@Embeddable
public class NumberOfGuests {

    private static final int MINIMUM_GUEST = 0;

    @Column(nullable = false)
    private int numberOfGuests;

    protected NumberOfGuests() {
    }

    private NumberOfGuests(int numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public static NumberOfGuests of(int numberOfGuests) {
        return new NumberOfGuests(numberOfGuests);
    }

    private void validateNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < MINIMUM_GUEST) {
            throw new IllegalArgumentException(NUMBER_OF_GUESTS_CANNOT_ZERO);
        }
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}

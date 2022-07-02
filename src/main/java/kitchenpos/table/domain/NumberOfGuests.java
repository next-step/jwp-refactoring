package kitchenpos.table.domain;

import static kitchenpos.Exception.InvalidNumberOfGuestsException.INVALID_NUMBER_OF_GUESTS;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {
    @Column(nullable = false)
    private int numberOfGuests;

    protected NumberOfGuests() {
    }

    private NumberOfGuests(int numberOfGuests) {
        validate(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public static NumberOfGuests from(int numberOfGuests) {
        return new NumberOfGuests(numberOfGuests);
    }

    public int getValue() {
        return numberOfGuests;
    }

    private void validate(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw INVALID_NUMBER_OF_GUESTS;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NumberOfGuests that = (NumberOfGuests) o;
        return numberOfGuests == that.numberOfGuests;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfGuests);
    }
}

package kitchenpos.table.domain;

import kitchenpos.table.exception.NegativeNumberOfGuestsException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class NumberOfGuests {
    private static final int MINIMUM = 0;

    @Column(nullable = false)
    private int numberOfGuests;

    protected NumberOfGuests() {
    }

    private NumberOfGuests(final int numberOfGuests) {
        validate(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public static NumberOfGuests from(final int numberOfGuests) {
        return new NumberOfGuests(numberOfGuests);
    }

    private void validate(final int quantity) {
        if (Objects.isNull(quantity) || quantity < MINIMUM) {
            throw new NegativeNumberOfGuestsException();
        }
    }

    public int toInt() {
        return numberOfGuests;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NumberOfGuests that = (NumberOfGuests) o;
        return numberOfGuests == that.numberOfGuests;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfGuests);
    }
}

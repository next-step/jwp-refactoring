package kitchenpos.table.domain;

import kitchenpos.table.domain.exception.NegativeNumberOfGuestsException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class NumberOfGuests {
    private static final int MIN_VALUE = 0;

    @Column(name = "number_of_guests", nullable = false)
    private final int value;

    protected NumberOfGuests() {
        this.value = 0;
    }

    private NumberOfGuests(int value) {
        validateValue(value);
        this.value = value;
    }

    private void validateValue(int value) {
        if (value < MIN_VALUE) {
            throw new NegativeNumberOfGuestsException(MIN_VALUE);
        }
    }

    public static NumberOfGuests of(int value) {
        return new NumberOfGuests(value);
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NumberOfGuests that = (NumberOfGuests) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

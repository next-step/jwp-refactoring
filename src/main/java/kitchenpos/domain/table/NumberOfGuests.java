package kitchenpos.domain.table;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {

    public static final String INVALID_NUMBER_OF_GUESTS = "손님(NumberOfGuest)은 0보다 작은을 수 없습니다. (input = %s)";
    private static final int MIN_NUMBER_OF_GUESTS = 0;

    @Column(name = "number_of_guests", nullable = false)
    private int value;

    protected NumberOfGuests() {}

    private NumberOfGuests(int numberOfGuests) {
        this.value = numberOfGuests;
    }

    public static NumberOfGuests createEmpty() {
        return new NumberOfGuests(MIN_NUMBER_OF_GUESTS);
    }

    public int getValue() {
        return this.value;
    }

    public static NumberOfGuests from(int numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);
        return new NumberOfGuests(numberOfGuests);
    }

    private static void validateNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < MIN_NUMBER_OF_GUESTS) {
            throw new IllegalArgumentException(String.format(INVALID_NUMBER_OF_GUESTS, numberOfGuests));
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
        return getValue() == that.getValue();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}

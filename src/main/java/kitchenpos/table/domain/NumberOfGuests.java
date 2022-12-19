package kitchenpos.table.domain;

import javax.persistence.Embeddable;
import java.util.Objects;

import static kitchenpos.table.domain.OrderTable.*;

@Embeddable
public class NumberOfGuests {

    private int numberOfGuests;

    protected NumberOfGuests() {

    }

    public NumberOfGuests(int numberOfGuests) {
        validate(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
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

    private static void validate(int numberOfGuests) {
        if (numberOfGuests < CHANGE_NUMBER_OF_GUESTS_MINIMUM_NUMBER) {
            throw new IllegalArgumentException(CHANGE_NUMBER_OF_GUESTS_MINIMUM_NUMBER_EXCEPTION_MESSAGE);
        }
    }
}

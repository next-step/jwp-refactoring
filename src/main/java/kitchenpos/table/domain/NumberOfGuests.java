package kitchenpos.table.domain;

import kitchenpos.ExceptionMessage;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class NumberOfGuests {

    private static final int MINIMUM_NUMBER = 0;

    private int numberOfGuests;

    protected NumberOfGuests() {

    }

    public NumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < MINIMUM_NUMBER) {
            throw new IllegalArgumentException(ExceptionMessage.GUEST_NUMBER_UNDER_MINIMUM
                    .getMessage("" + MINIMUM_NUMBER));
        }
        this.numberOfGuests = numberOfGuests;
    }

    public int getValue() {
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

package kitchenpos.ordertable.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.constants.ErrorMessages;

@Embeddable
public class NumberOfGuests {

    private static final int MIN_NUMBER_OF_GUESTS = 0;
    @Column(nullable = false)
    private int numberOfGuests;

    public NumberOfGuests() {
        this.numberOfGuests = 0;
    }

    public NumberOfGuests(int numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validateNumberOfGuests(int number) {
        if (number < MIN_NUMBER_OF_GUESTS) {
            throw new IllegalArgumentException(ErrorMessages.NUMBER_OF_GUESTS_CANNOT_BE_LESS_THAN_ZERO);
        }
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
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

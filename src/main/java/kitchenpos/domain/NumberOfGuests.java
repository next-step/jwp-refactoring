package kitchenpos.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.exception.ExceptionMessage;
import kitchenpos.exception.InvalidNumberOfGuestsSize;

@Embeddable
public class NumberOfGuests {
    @Column(nullable = false)
    private int numberOfGuests;

    protected NumberOfGuests() {
    }

    private NumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public static NumberOfGuests from(int numberOfGuests) {
        checkNumberOfGuestsGreaterOrEqualsZero(numberOfGuests);
        return new NumberOfGuests(numberOfGuests);
    }

    private static void checkNumberOfGuestsGreaterOrEqualsZero(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new InvalidNumberOfGuestsSize(ExceptionMessage.INVALID_NUMBER_OF_GUESTS_SIZE);
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

package kitchenpos.domain.order;

import kitchenpos.exception.BadRequestException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

import static kitchenpos.utils.Message.INVALID_UNDER_ZERO_GUESTS;

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
            throw new BadRequestException(INVALID_UNDER_ZERO_GUESTS);
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

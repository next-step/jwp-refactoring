package kitchenpos.table.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.common.exception.InvalidGuestNumberException;

@Embeddable
public class GuestNumber {
    private static final int MIN_NUMBER = 0;

    @Column(nullable = false)
    private int numberOfGuests;

    protected GuestNumber() {
    }

    public GuestNumber(int number) {
        validateNumber(number);
        this.numberOfGuests = number;
    }

    private void validateNumber(int number) {
        if (number < MIN_NUMBER) {
            throw new InvalidGuestNumberException();
        }
    }

    public static GuestNumber of(int number) {
        return new GuestNumber(number);
    }

    public int number() {
        return this.numberOfGuests;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GuestNumber that = (GuestNumber) o;
        return numberOfGuests == that.numberOfGuests;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfGuests);
    }
}

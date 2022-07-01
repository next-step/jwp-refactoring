package kitchenpos.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {
    public static final int MIN = 0;

    @Column(nullable = false)
    private int numberOfGuests;

    protected NumberOfGuests() {
    }

    private NumberOfGuests(int numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public static NumberOfGuests from(int numberOfGuests) {
        return new NumberOfGuests(numberOfGuests);
    }

    private static void validateNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < MIN) {
            throw new IllegalArgumentException("손님수가 " + MIN + "미만일 수 없습니다.");
        }
    }

    public int value() {
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
        NumberOfGuests that = (NumberOfGuests) o;
        return numberOfGuests == that.numberOfGuests;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfGuests);
    }

    @Override
    public String toString() {
        return String.valueOf(numberOfGuests);
    }
}

package kitchenpos.table.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class NumberOfGuests {
    private static final int MIN_VALUE = 0;

    @Column(nullable = false)
    private int numberOfGuests;

    protected NumberOfGuests() {}

    public NumberOfGuests(int value) {
        checkNumberOfGuests(value);

        this.numberOfGuests = value;
    }

    public int getValue() {
        return numberOfGuests;
    }

    private void checkNumberOfGuests(int value) {
        if (value < MIN_VALUE) {
            throw new IllegalArgumentException("손님의 수는 0보다 작을 수 없습니다.");
        }
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

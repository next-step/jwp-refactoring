package kitchenpos.table;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {

    @Column(name = "numberOfGuests")
    private int numberOfGuests;

    protected NumberOfGuests() {
    }

    private NumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님의 수는 0보다 커야 합니다");
        }
        this.numberOfGuests = numberOfGuests;
    }

    public static NumberOfGuests of(int numberOfGuests) {
        return new NumberOfGuests(numberOfGuests);
    }

    public int getValue() {
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

    @Override
    public String toString() {
        return "NumberOfGuests{" +
            "numberOfGuests=" + numberOfGuests +
            '}';
    }
}

package kitchenpos.table.domain;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class NumberOfGuests {
    private int numberOfGuests;

    public NumberOfGuests() {
    }

    public NumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int value() {
        return numberOfGuests;
    }

    public boolean lessThenZero() {
        return this.numberOfGuests < 0;
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

    @Override
    public String toString() {
        return "NumberOfGuests{" +
                "numberOfGuests=" + numberOfGuests +
                '}';
    }
}

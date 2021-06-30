package kitchenpos.domain;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class NumberOfGuest {
    private int numberOfGuests;

    protected NumberOfGuest() {
    }

    public NumberOfGuest(int numberOfGuests) {
        validate(numberOfGuests);

        this.numberOfGuests = numberOfGuests;
    }

    private void validate(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
    }

    public int toInt() {
        return numberOfGuests;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NumberOfGuest that = (NumberOfGuest) o;
        return numberOfGuests == that.numberOfGuests;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfGuests);
    }
}

package kitchenpos.ordertable.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class NumberOfGuests {

    private static final int MIN_NUMBER_OF_GUESTS = 0;

    @Column(name = "number_of_guests")
    private int numberOfGuests;

    protected NumberOfGuests() {
    }

    public NumberOfGuests(final int value) {
        if (value < MIN_NUMBER_OF_GUESTS) {
            throw new IllegalArgumentException("손님 수는 0명 이상이어야 합니다.");
        }
        this.numberOfGuests = value;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof NumberOfGuests)) return false;
        final NumberOfGuests that = (NumberOfGuests) o;
        return numberOfGuests == that.numberOfGuests;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfGuests);
    }
}

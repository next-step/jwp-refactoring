package kitchenpos.orders.table.domain;

import java.util.Objects;
import javax.persistence.Column;

public class NumberOfGuests {

    @Column(nullable = false)
    private int numberOfGuests;

    public NumberOfGuests() {
    }

    public NumberOfGuests(int numberOfGuests) {
        validateNumberOfGuest(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validateNumberOfGuest(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님수는 0명 이상이어야 합니다.");
        }
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
}

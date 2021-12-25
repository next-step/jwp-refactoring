package kitchenpos.ordertable.domain;

import java.util.Objects;
import javax.persistence.Embeddable;
import kitchenpos.ordertable.exception.IllegalNumberOfGuests;

@Embeddable
public class NumberOfGuests {
    
    private int numberOfGuests;

    protected NumberOfGuests() {
    }

    public NumberOfGuests(int numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validateNumberOfGuests(int inputNumberOfGuest) {
        if (inputNumberOfGuest < 0) {
            throw new IllegalNumberOfGuests();
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
        return getNumberOfGuests() == that.getNumberOfGuests();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNumberOfGuests());
    }
}

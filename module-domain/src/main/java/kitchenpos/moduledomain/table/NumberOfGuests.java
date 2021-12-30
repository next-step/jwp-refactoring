package kitchenpos.moduledomain.table;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.moduledomain.common.exception.Message;

@Embeddable
public class NumberOfGuests {

    @Column
    private int numberOfGuests;

    public NumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException(
                Message.NUMBER_OF_GUEST_SMALL_THAN_ZERO.getMessage());
        }
        this.numberOfGuests = numberOfGuests;
    }

    protected NumberOfGuests() {
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

package kitchenpos.table.domain;

import kitchenpos.exception.GuestsNumberNegativeException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class NumberOfGuests {
    private static final int MIN_NUMBER = 0;
    @Column
    private int numberOfGuests;

    protected NumberOfGuests() {
    }

    public NumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
        validateNumber();
    }

    private void validateNumber() {
        if(numberOfGuests < MIN_NUMBER){
            throw new GuestsNumberNegativeException();
        }
    }

    public int getNumber() {
        return numberOfGuests;
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

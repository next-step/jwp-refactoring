package kitchenpos.table.domain;

import kitchenpos.table.exception.InvalidNumberOfGuestsException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class NumberOfGuests implements Comparable<NumberOfGuests> {

    @Column(nullable = false)
    private int numberOfGuests;

    public NumberOfGuests(int numberOfGuests) {
        validate(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    protected NumberOfGuests() {
    }

    private void validate(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new InvalidNumberOfGuestsException();
        }
    }

    public int getValue() {
        return numberOfGuests;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NumberOfGuests that = (NumberOfGuests) o;
        return compareTo(that) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfGuests);
    }

    @Override
    public int compareTo(NumberOfGuests o) {
        return Integer.compare(numberOfGuests, o.numberOfGuests);
    }
}

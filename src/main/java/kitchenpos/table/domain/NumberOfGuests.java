package kitchenpos.table.domain;

import kitchenpos.table.message.NumberOfGuestsMessage;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {

    @Column(nullable = false)
    private final Integer numberOfGuests;

    protected NumberOfGuests() {
        this.numberOfGuests = null;
    }

    private NumberOfGuests(Integer numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validateNumberOfGuests(Integer numberOfGuests) {
        if(numberOfGuests == null) {
            throw new IllegalArgumentException(NumberOfGuestsMessage.CREATE_ERROR_GUESTS_MUST_BE_NOT_NULL.message());
        }

        if (numberOfGuests < 0) {
            throw new IllegalArgumentException(NumberOfGuestsMessage.CREATE_ERROR_GUESTS_MUST_BE_MORE_THAN_ZERO.message());
        }
    }

    public static NumberOfGuests of(Integer numberOfGuests) {
        return new NumberOfGuests(numberOfGuests);
    }

    public Integer value() {
        return this.numberOfGuests;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NumberOfGuests that = (NumberOfGuests) o;

        return numberOfGuests.equals(that.numberOfGuests);
    }

    @Override
    public int hashCode() {
        return numberOfGuests.hashCode();
    }

    @Override
    public String toString() {
        return String.valueOf(numberOfGuests);
    }
}

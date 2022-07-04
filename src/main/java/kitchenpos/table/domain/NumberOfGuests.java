package kitchenpos.table.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {
    @Column(name = "number_of_guests")
    private int numberOfGuests;

    public NumberOfGuests() {
    }

    public NumberOfGuests(final int numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validateNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님 수는 0보다 작을 수 없습니다.");
        }
    }

    public int value() {
        return numberOfGuests;
    }
}

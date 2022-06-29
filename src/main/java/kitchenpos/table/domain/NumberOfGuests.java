package kitchenpos.table.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {
    private static final int MINIMUM_NUMBER_OF_GUESTS = 0;

    @Column(name = "number_of_guests", nullable = false)
    private int value;

    protected NumberOfGuests() {
    }

    public NumberOfGuests(final int value) {
        validate(value);
        this.value = value;
    }

    private void validate(final int value) {
        if (value < MINIMUM_NUMBER_OF_GUESTS) {
            throw new IllegalArgumentException("손님 수는 0 이상이어야 합니다.");
        }
    }

    public int getValue() {
        return value;
    }
}

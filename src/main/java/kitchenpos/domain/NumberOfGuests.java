package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {
    private static final int MINIMUM_NUMBER_OF_GUESTS = 0;

    @Column(name = "number_of_guests", nullable = false)
    private int value;

    protected NumberOfGuests() {
    }

    public NumberOfGuests(int value) {
        validate(value);
        this.value = value;
    }

    private void validate(int value) {
        if (value < MINIMUM_NUMBER_OF_GUESTS) {
            throw new IllegalArgumentException("손님 수는 0 이상이어야 합니다.");
        }
    }

    public int getValue() {
        return value;
    }
}

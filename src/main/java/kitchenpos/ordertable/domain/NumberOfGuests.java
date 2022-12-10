package kitchenpos.ordertable.domain;

import javax.persistence.*;

@Embeddable
public class NumberOfGuests {
    private static final int ZERO = 0;

    private int numberOfGuests;

    protected NumberOfGuests() {}

    public NumberOfGuests(int numberOfGuests) {
        validate(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validate(int numberOfGuests) {
        if (numberOfGuests < ZERO) {
            throw new IllegalArgumentException("손님수는 0보다 작을 수 없습니다.");
        }
    }

    public int value() {
        return numberOfGuests;
    }
}

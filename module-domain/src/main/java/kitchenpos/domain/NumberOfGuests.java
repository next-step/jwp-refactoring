package kitchenpos.domain;

import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {
    private static final int ZERO_VALUE = 0;
    private int numberOfGuests;

    protected NumberOfGuests() {
    }

    public NumberOfGuests(int numberOfGuests) {
        update(numberOfGuests);
    }

    public void update(int numberOfGuests) {
        validate(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validate(int numberOfGuests) {
        if (numberOfGuests < ZERO_VALUE) {
            throw new IllegalArgumentException("손님 수는 0 보다 작을 수 없습니다.");
        }
    }

    public int value() {
        return numberOfGuests;
    }
}

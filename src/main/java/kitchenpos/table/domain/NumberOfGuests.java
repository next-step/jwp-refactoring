package kitchenpos.table.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {
    @Column(name = "numberOfGuests", nullable = false)
    private int value;

    protected NumberOfGuests() {
        this(0);
    }

    protected NumberOfGuests(int numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);
        this.value = numberOfGuests;
    }

    public static NumberOfGuests from(int numberOfGuests) {
        return new NumberOfGuests(numberOfGuests);
    }

    private void validateNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님 수가 0보다 작을 수 없습니다.");
        }
    }

    public int value() {
        return value;
    }
}

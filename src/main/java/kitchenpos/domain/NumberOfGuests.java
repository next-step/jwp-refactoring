package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {
    public static final int MIN = 0;

    @Column(nullable = false)
    private int numberOfGuests;

    protected NumberOfGuests() {}

    private NumberOfGuests(int numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public static NumberOfGuests of(int numberOfGuests) {
        return new NumberOfGuests(numberOfGuests);
    }

    private static void validateNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < MIN) {
            throw new IllegalArgumentException("손님수가 " + MIN + "미만일 수 없습니다.");
        }
    }

    public int value() {
        return this.numberOfGuests;
    }
}

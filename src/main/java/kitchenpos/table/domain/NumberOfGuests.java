package kitchenpos.table.domain;

import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {
    private static final int MIN_NUMBER_OF_GUESTS = 0;

    private int numberOfGuests;

    protected NumberOfGuests() {
    }

    private NumberOfGuests(int numberOfGuests) {
        validate(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validate(int numberOfGuests) {
        if (numberOfGuests < MIN_NUMBER_OF_GUESTS) {
            throw new IllegalArgumentException("방문한 손님수가 0미만일 수 없습니다.");
        }
    }

    public static NumberOfGuests from(int numberOfGuests) {
        return new NumberOfGuests(numberOfGuests);
    }

    public int value() {
        return numberOfGuests;
    }
}

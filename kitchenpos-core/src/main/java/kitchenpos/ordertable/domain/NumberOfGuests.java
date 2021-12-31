package kitchenpos.ordertable.domain;

import kitchenpos.ordertable.exception.IllegalOrderTableIdsException;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {
    private static final String MIN_NUMBER_OF_GUEST_ERROR_MESSAGE = "방문한 손님 수는 1명 이상이어야 한다.";
    private static final int MIN_VALUE = 1;

    @Column(nullable = false)
    private int numberOfGuests;

    public NumberOfGuests() {
    }

    private NumberOfGuests(int numberOfGuests) {
        validate(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validate(int numberOfGuests) {
        if (numberOfGuests < MIN_VALUE) {
            throw new IllegalOrderTableIdsException(MIN_NUMBER_OF_GUEST_ERROR_MESSAGE);
        }
    }

    public static NumberOfGuests of(int numberOfGuest) {
        return new NumberOfGuests(numberOfGuest);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean matchNumberOfGuests(int targetNumberOfGuests) {
        return this.numberOfGuests == targetNumberOfGuests;
    }
}

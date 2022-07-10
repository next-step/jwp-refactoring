package kitchenpos.table.domain;

import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuest {
    private static final int MIN_NUMBER_OF_GUEST = 0;

    private Integer numberOfGuests = 0;

    protected NumberOfGuest() {

    }

    private NumberOfGuest(int numberOfGuests) {
        validateNumberOfGuest(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public static NumberOfGuest from(int numberOfGuest) {
        return new NumberOfGuest(numberOfGuest);
    }

    private void validateNumberOfGuest(int numberOfGuests) {
        if (numberOfGuests < MIN_NUMBER_OF_GUEST) {
            throw new IllegalArgumentException("사람의 수는 음수일수가 없습니다.");
        }
    }

    public int value() {
        return numberOfGuests;
    }
}

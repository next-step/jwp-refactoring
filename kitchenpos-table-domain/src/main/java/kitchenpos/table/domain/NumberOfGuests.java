package kitchenpos.table.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {

    static final int MINIMUM_GUEST_NUMBER = 0;

    @Column(nullable = false)
    private int numberOfGuests;

    protected NumberOfGuests() {
    }

    private NumberOfGuests(final int numberOfGuests) {
        validateGreaterOrEqualsZero(numberOfGuests);

        this.numberOfGuests = numberOfGuests;
    }

    public static NumberOfGuests of(final int numberOfGuests) {
        return new NumberOfGuests(numberOfGuests);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    private void validateGreaterOrEqualsZero(int numberOfGuests) {
        if (numberOfGuests < MINIMUM_GUEST_NUMBER) {
            throw new IllegalArgumentException(
                String.format("손님의 수는 %d 명 이상이어야 합니다.", MINIMUM_GUEST_NUMBER));
        }
    }
}

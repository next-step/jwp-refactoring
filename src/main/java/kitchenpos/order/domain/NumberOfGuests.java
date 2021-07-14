package kitchenpos.order.domain;

import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {
    private int numberOfGuests;

    public static NumberOfGuests of(int numberOfGuests) {
        return new NumberOfGuests(numberOfGuests);
    }

    protected NumberOfGuests() {
    }

    public NumberOfGuests(int numberOfGuests) {
        checkArgument(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void checkArgument(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님 수는 음수가 될 수 없습니다.");
        }
    }

    public int value() {
        return numberOfGuests;
    }
}

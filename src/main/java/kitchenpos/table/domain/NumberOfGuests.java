package kitchenpos.table.domain;

import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {
    private int numberOfGuests;

    protected NumberOfGuests() {
    }

    private NumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public static NumberOfGuests of(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님의 수는 0보다 작을 수 없습니다.");
        }
        return new NumberOfGuests(numberOfGuests);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}

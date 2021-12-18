package kitchenpos.domain;

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
            throw new IllegalArgumentException("0 이상의 숫자를 입력하세요");
        }
        return new NumberOfGuests(numberOfGuests);
    }

    public int value() {
        return numberOfGuests;
    }
}

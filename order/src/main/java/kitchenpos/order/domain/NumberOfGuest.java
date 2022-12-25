package kitchenpos.order.domain;

import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuest {

    private static final int MIN_NUMBER_OF_GUEST = 0;

    private int numberOfGuests;

    protected NumberOfGuest() {
    }

    public NumberOfGuest(int numberOfGuests) {
        if (numberOfGuests < MIN_NUMBER_OF_GUEST) {
            throw new IllegalArgumentException("손님의 수는 0명 이하일 수 없습니다.");
        }
        this.numberOfGuests = numberOfGuests;
    }

    public int numberOfGuest() {
        return numberOfGuests;
    }
}

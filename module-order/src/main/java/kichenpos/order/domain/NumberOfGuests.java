package kichenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {
    public static final int MINIMUM_GUEST_NUMBER = 0;

    @Column(name = "number_of_guests")
    private int value;

    protected NumberOfGuests() {
    }

    public NumberOfGuests(int numberOfGuests) {
        validateGuestNumber(numberOfGuests);
        this.value = numberOfGuests;
    }

    private void validateGuestNumber(int numberOfGuests) {
        if (numberOfGuests < MINIMUM_GUEST_NUMBER) {
            throw new IllegalArgumentException("방문한 손님의 수가 0보다 작으면 손님의 수를 변경할 수 없습니다.");
        }
    }

    public int value() {
        return value;
    }
}

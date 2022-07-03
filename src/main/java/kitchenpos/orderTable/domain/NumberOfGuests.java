package kitchenpos.orderTable.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {
    public static final int MIN = 0;

    @Column(name = "number_of_guests")
    private int value;

    protected NumberOfGuests() {
    }

    public NumberOfGuests(int value) {
        if (value < MIN) {
            throw new IllegalArgumentException("변경할 손님의 수는 최소 " + MIN + "이상이어야 합니다.");
        }

        this.value = value;
    }

    public boolean isZero() {
        return value == MIN;
    }

    public int getValue() {
        return value;
    }
}

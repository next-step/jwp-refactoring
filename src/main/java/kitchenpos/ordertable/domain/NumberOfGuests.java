package kitchenpos.ordertable.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {
    private static final int MIN_NUMBER_OF_GUESTS = 0;
    private static final String NUMBER_OF_GUESTS_CAN_NOT_NEGATIVE = "손님 수는 음수 일 수 없습니다.";

    @Column(name = "number_of_guests")
    private int value;

    protected NumberOfGuests() {}

    public NumberOfGuests(int value) {
        validate(value);
        this.value = value;
    }

    public void validate(int value) {
        if (value < MIN_NUMBER_OF_GUESTS) {
            throw new IllegalArgumentException(NUMBER_OF_GUESTS_CAN_NOT_NEGATIVE);
        }
    }

    public int value() {
        return value;
    }
}

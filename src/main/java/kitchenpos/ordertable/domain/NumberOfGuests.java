package kitchenpos.ordertable.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import kitchenpos.common.error.CustomException;
import kitchenpos.common.error.ErrorInfo;

@Embeddable
public class NumberOfGuests {
    @Column(name = "number_of_guests")
    private final int number;

    public NumberOfGuests() {
        this.number = 0;
    }

    public NumberOfGuests(int number) {
        checkNegative(number);
        this.number = number;
    }

    private void checkNegative(int number) {
        if (number < 0) {
            throw new CustomException(ErrorInfo.INVALID_NUMBER_OF_GUESTS);
        }
    }

    public int number() {
        return number;
    }
}

package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import kitchenpos.error.NegativeValueException;

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
            throw new NegativeValueException();
        }
    }

    public int number() {
        return number;
    }
}

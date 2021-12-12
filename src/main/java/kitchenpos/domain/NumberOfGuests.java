package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {

    private final int MIN_NUMBER_OF_GUESTS = 0;

    @Column
    private int number;

    protected NumberOfGuests() {
    }

    public NumberOfGuests(int number) {
        check(number);
        this.number = number;
    }

    private void check(int number) {
        if (number < MIN_NUMBER_OF_GUESTS) {
            throw new IllegalArgumentException();
        }
    }

    public int getNumber() {
        return number;
    }
}

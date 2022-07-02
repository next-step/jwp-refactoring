package kitchenpos.table.domain;

import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {
    public static final int MIN_NUMBEROFGUESTS = 0;

    private int element;

    protected NumberOfGuests() {
    }

    public NumberOfGuests(int numberOfGuests) {
        validate(numberOfGuests);
        this.element = numberOfGuests;
    }

    private void validate(int numberOfGuests) {
        if (numberOfGuests < MIN_NUMBEROFGUESTS) {
            throw new IllegalArgumentException("손님 수는 0 미만일 수 없습니다.");
        }
    }

    public int get() {
        return element;
    }

    public void change(int numberOfGuests) {
        validate(numberOfGuests);
        this.element = numberOfGuests;
    }
}

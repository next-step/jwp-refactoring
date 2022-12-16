package kitchenpos.table.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {

    private static final int MIN_GUEST_NUMBER = 0;

    @Column(nullable = false)
    private int numberOfGuests;

    protected NumberOfGuests() {
    }

    private NumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public static NumberOfGuests of(int numberOfGuests) {
        validate(numberOfGuests);
        return new NumberOfGuests(numberOfGuests);
    }

    public void change(int numberOfGuests) {
        validate(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private static void validate(int numberOfGuests) {
        if (numberOfGuests < MIN_GUEST_NUMBER) {
            throw new IllegalArgumentException("손님의 수는 0보다 작을 수 없습니다.");
        }
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}

package kitchenpos.domain;

public class NumberOfGuests {
    private int numberOfGuests;

    public NumberOfGuests(final int numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validateNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님 수는 0보다 작을 수 없습니다.");
        }
    }

    public int value() {
        return numberOfGuests;
    }
}

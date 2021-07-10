package kitchenpos.common;

public class NumberOfGuests {
    private final int number;

    public NumberOfGuests() {
        this.number = 0;
    }

    public NumberOfGuests(int number) {
        validNegative(number);
        this.number = number;
    }

    private void validNegative(int number) {
        if (number < 0) {
            throw new IllegalArgumentException("변경하려는 인원수는 0보다 커야합니다.");
        }
    }

    public int number() {
        return number;
    }
}

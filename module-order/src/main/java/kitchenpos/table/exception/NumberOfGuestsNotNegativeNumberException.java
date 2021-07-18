package kitchenpos.table.exception;

public class NumberOfGuestsNotNegativeNumberException extends IllegalArgumentException {

    public NumberOfGuestsNotNegativeNumberException() {
        super("고객 수는 0보다 작을 수 없습니다.");
    }

}

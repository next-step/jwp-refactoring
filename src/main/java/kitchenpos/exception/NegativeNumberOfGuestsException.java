package kitchenpos.exception;

public class NegativeNumberOfGuestsException extends RuntimeException {
    public NegativeNumberOfGuestsException() {
        super("손님의 숫자는 음수가 될 수 없습니다");
    }
}

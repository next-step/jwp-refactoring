package kitchenpos.exception;

public class InvalidNumberOfGuestsException extends RuntimeException {
    private static final String message = "손님의 수는 0보다 작을 수 없습니다.";
    public static final InvalidNumberOfGuestsException INVALID_NUMBER_OF_GUESTS = new InvalidNumberOfGuestsException();

    public InvalidNumberOfGuestsException() {
        super(message);
    }
}

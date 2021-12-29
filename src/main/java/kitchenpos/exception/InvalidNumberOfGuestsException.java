package kitchenpos.exception;

public class InvalidNumberOfGuestsException extends IllegalArgumentException {
    public InvalidNumberOfGuestsException(String message) {
        super(message);
    }
}

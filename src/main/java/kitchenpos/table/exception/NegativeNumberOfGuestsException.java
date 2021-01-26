package kitchenpos.table.exception;

public class NegativeNumberOfGuestsException extends RuntimeException {
    public NegativeNumberOfGuestsException(String message) {
        super(message);
    }
}

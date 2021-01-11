package kitchenpos.domain.orderTable.exceptions;

public class InvalidNumberOfGuestsException extends RuntimeException {
    public InvalidNumberOfGuestsException(final String message) {
        super(message);
    }
}

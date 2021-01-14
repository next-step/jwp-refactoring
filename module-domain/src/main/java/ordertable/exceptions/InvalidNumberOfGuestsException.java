package kitchenpos.domain.ordertable.exceptions;

public class InvalidNumberOfGuestsException extends RuntimeException {
    public InvalidNumberOfGuestsException(final String message) {
        super(message);
    }
}

package kitchenpos.exception;

public class FailedChangeNumberOfGuestsException extends RuntimeException {

    public FailedChangeNumberOfGuestsException() {
    }

    public FailedChangeNumberOfGuestsException(String message) {
        super(message);
    }
}

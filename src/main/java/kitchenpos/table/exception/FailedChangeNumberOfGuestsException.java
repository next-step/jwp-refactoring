package kitchenpos.table.exception;

public class FailedChangeNumberOfGuestsException extends RuntimeException {

    public FailedChangeNumberOfGuestsException() {
    }

    public FailedChangeNumberOfGuestsException(String message) {
        super(message);
    }
}

package kitchenpos.exception;

public class FailedChangeEmptyException extends RuntimeException {

    public FailedChangeEmptyException() {
    }

    public FailedChangeEmptyException(String message) {
        super(message);
    }
}

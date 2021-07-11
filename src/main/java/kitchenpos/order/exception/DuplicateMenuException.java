package kitchenpos.order.exception;

public class DuplicateMenuException extends RuntimeException {

    public DuplicateMenuException() {
    }

    public DuplicateMenuException(String message) {
        super(message);
    }
}

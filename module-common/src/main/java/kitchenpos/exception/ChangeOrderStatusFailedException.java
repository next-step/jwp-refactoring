package kitchenpos.exception;

public class ChangeOrderStatusFailedException extends RuntimeException {

    public ChangeOrderStatusFailedException() {
    }

    public ChangeOrderStatusFailedException(String message) {
        super(message);
    }
}

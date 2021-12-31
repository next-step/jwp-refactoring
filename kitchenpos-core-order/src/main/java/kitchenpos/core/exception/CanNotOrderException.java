package kitchenpos.core.exception;

public class CanNotOrderException extends IllegalArgumentException {
    public CanNotOrderException(String message) {
        super(message);
    }
}

package kitchenpos.core.exception;

public class CanNotChangeOrderTableException extends IllegalArgumentException {
    public CanNotChangeOrderTableException(String message) {
        super(message);
    }
}

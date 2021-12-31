package kitchenpos.core.exception;

public class CanNotChangeOrderStatusException extends IllegalArgumentException {
    public CanNotChangeOrderStatusException(String message) {
        super(message);
    }
}

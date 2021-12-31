package kitchenpos.ordertable.exception;

public class CanNotChangeOrderTableException extends IllegalArgumentException {
    public CanNotChangeOrderTableException(String message) {
        super(message);
    }
}

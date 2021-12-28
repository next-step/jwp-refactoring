package kitchenpos.ordertable.application;

public class CanNotChangeOrderTableException extends IllegalArgumentException {
    public CanNotChangeOrderTableException(String message) {
        super(message);
    }
}

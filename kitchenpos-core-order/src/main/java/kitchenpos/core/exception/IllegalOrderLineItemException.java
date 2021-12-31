package kitchenpos.core.exception;

public class IllegalOrderLineItemException extends IllegalArgumentException {
    public IllegalOrderLineItemException(String message) {
        super(message);
    }
}

package kitchenpos.exception;

public class EmptyOrderLineItemException extends RuntimeException {
    public EmptyOrderLineItemException(String message) {
        super(message);
    }
}

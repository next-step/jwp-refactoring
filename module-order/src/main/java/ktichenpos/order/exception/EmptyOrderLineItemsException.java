package ktichenpos.order.exception;

public class EmptyOrderLineItemsException extends RuntimeException {
    public EmptyOrderLineItemsException() {
    }

    public EmptyOrderLineItemsException(String message) {
        super(message);
    }

    public EmptyOrderLineItemsException(String message, Throwable cause) {
        super(message, cause);
    }
}

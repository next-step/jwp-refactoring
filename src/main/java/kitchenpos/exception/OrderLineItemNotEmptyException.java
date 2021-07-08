package kitchenpos.exception;

public class OrderLineItemNotEmptyException extends RuntimeException {
    public OrderLineItemNotEmptyException() {
    }

    public OrderLineItemNotEmptyException(String s) {
        super(s);
    }

    public OrderLineItemNotEmptyException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderLineItemNotEmptyException(Throwable cause) {
        super(cause);
    }
}

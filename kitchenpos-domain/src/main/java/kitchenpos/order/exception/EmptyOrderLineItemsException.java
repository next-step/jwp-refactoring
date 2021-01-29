package kitchenpos.order.exception;

public class EmptyOrderLineItemsException extends RuntimeException {
    public EmptyOrderLineItemsException(String message) {
        super(message);
    }
}

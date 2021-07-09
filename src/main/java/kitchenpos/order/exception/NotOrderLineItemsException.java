package kitchenpos.order.exception;

public class NotOrderLineItemsException extends IllegalArgumentException {

    public NotOrderLineItemsException(String message) {
        super(message);
    }
}

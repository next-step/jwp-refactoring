package kitchenpos.exception.order;


public class InvalidOrderLineItemsException extends IllegalArgumentException {

    public InvalidOrderLineItemsException(String s) {
        super(s);
    }
}

package kitchenpos.order.exception;

public class EmptyOrderLineItemOrderException extends IllegalArgumentException{
    public EmptyOrderLineItemOrderException() {
        super();
    }

    public EmptyOrderLineItemOrderException(String string) {
        super(string);
    }
}

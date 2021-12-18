package kitchenpos.exception.order;

public class EmptyOrderLineItemOrderException extends IllegalArgumentException{
    public EmptyOrderLineItemOrderException() {
        super();
    }

    public EmptyOrderLineItemOrderException(String string) {
        super(string);
    }
}

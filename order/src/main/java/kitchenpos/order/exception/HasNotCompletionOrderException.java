package kitchenpos.order.exception;

public class HasNotCompletionOrderException extends IllegalArgumentException{
    public HasNotCompletionOrderException() {
        super();
    }

    public HasNotCompletionOrderException(String string) {
        super(string);
    }
}

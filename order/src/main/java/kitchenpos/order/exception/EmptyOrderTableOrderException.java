package kitchenpos.order.exception;

public class EmptyOrderTableOrderException extends IllegalArgumentException{
    public EmptyOrderTableOrderException() {
        super();
    }

    public EmptyOrderTableOrderException(String string) {
        super(string);
    }
}

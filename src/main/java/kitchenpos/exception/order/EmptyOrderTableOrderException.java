package kitchenpos.exception.order;

public class EmptyOrderTableOrderException extends IllegalArgumentException{
    public EmptyOrderTableOrderException() {
        super();
    }

    public EmptyOrderTableOrderException(String string) {
        super(string);
    }
}

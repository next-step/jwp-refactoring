package kitchenpos.order.exception;

public class NotFoundOrderException extends IllegalArgumentException{
    public NotFoundOrderException() {
        super();
    }

    public NotFoundOrderException(String string) {
        super(string);
    }
}

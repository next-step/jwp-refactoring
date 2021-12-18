package kitchenpos.exception.order;

public class NotFoundOrderException extends IllegalArgumentException{
    public NotFoundOrderException() {
        super();
    }

    public NotFoundOrderException(String string) {
        super(string);
    }
}

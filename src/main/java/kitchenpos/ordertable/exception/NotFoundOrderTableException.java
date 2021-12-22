package kitchenpos.ordertable.exception;

public class NotFoundOrderTableException extends IllegalArgumentException{
    public NotFoundOrderTableException(String message) {
        super(message);
    }
}

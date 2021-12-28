package kitchenpos.ordertable.application;

public class NotFoundOrderTableException extends IllegalArgumentException{
    public NotFoundOrderTableException(String message) {
        super(message);
    }
}

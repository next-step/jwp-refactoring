package kitchenpos.order.exception;

public class BadOrderRequestException extends RuntimeException {
    public BadOrderRequestException(String message) {
        super(message);
    }
}

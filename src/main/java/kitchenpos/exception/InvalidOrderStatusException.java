package kitchenpos.exception;

public class InvalidOrderStatusException extends IllegalArgumentException {
    public InvalidOrderStatusException(String message) {
        super(message);
    }
}

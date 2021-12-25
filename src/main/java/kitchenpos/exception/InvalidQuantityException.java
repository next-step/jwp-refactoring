package kitchenpos.exception;

public class InvalidQuantityException extends IllegalArgumentException {
    public InvalidQuantityException(String message) {
        super(message);
    }
}

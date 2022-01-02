package order.exception;

public class NotChangeOrderStatusException extends RuntimeException {
    public NotChangeOrderStatusException(String message) {
        super(message);
    }
}

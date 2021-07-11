package kitchenpos.exception;

public class NotEnoughTablesException extends RuntimeException {
    public NotEnoughTablesException(String message, Object... args) {
        super(String.format(message, args));
    }
}

package kitchenpos.core.exception;

public class NotFoundMenuGroupException extends IllegalArgumentException {
    public NotFoundMenuGroupException(String message) {
        super(message);
    }
}

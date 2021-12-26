package kitchenpos.exception;

public class InvalidTableGroupException extends IllegalArgumentException {
    public InvalidTableGroupException(String message) {
        super(message);
    }
}

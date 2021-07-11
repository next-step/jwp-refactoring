package kitchenpos.common.exception;

public class InvalidQuantityException extends IllegalArgumentException {
    public InvalidQuantityException() {
    }

    public InvalidQuantityException(String s) {
        super(s);
    }
}

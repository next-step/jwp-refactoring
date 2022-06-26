package kitchenpos.exception;

public class InvalidQuantityException extends IllegalArgumentException {
    public InvalidQuantityException() {
        super("올바르지 않은 수량입니다.");
    }

    public InvalidQuantityException(String s) {
        super(s);
    }
}

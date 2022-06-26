package kitchenpos.exception;

public class InvalidPriceException extends IllegalArgumentException {

    public InvalidPriceException() {
        super("올바르지 않은 가격입니다.");
    }

    public InvalidPriceException(String s) {
        super(s);
    }
}

package kitchenpos.exception;

public class InvalidPriceException extends RuntimeException {
    public static final InvalidPriceException INVALID_PRICE_EXCEPTION = new InvalidPriceException("가격은 0이상 이어야 합니다.");

    public InvalidPriceException(String message) {
        super(message);
    }
}

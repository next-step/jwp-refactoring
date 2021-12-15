package kitchenpos.exception;

public class NegativePriceException extends RuntimeException {
    private static final String NEGATIVE_PRICE = "Price 는 0 이상의 값을 가집니다.";

    public NegativePriceException() {
        super(NEGATIVE_PRICE);
    }
}

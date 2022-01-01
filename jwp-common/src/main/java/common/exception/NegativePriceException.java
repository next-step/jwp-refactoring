package common.exception;

public class NegativePriceException extends CustomException {
    public static final String NEGATIVE_PRICE_MESSAGE = "가격은 0 미만일 수 없습니다.";

    public NegativePriceException() {
        super(NEGATIVE_PRICE_MESSAGE);
    }
}

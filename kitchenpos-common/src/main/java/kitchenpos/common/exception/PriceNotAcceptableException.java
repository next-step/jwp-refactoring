package kitchenpos.common.exception;

public class PriceNotAcceptableException extends RuntimeException {

    private final static String ERROR_MESSAGE_PRODUCT_PRICE_VALUE = "가격은 0원 이상이어야 합니다.";

    public PriceNotAcceptableException() {
        super(ERROR_MESSAGE_PRODUCT_PRICE_VALUE);
    }

    public PriceNotAcceptableException(String message) {
        super(message);
    }
}

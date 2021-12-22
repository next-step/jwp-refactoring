package kitchenpos.exception;

public class PriceValueNotAcceptableException extends RuntimeException {

    private final static String ERROR_MESSAGE_PRODUCT_PRICE_VALUE = "상품의 가격은 0원 이상이어야 합니다.";

    public PriceValueNotAcceptableException() {
        super(ERROR_MESSAGE_PRODUCT_PRICE_VALUE);
    }

    public PriceValueNotAcceptableException(String message) {
        super(message);
    }
}

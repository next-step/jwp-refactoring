package kitchenpos.product.exception;

public class ProductPriceNotAcceptableException extends RuntimeException {

    private final static String ERROR_MESSAGE_PRODUCT_PRICE_VALUE = "상품의 가격은 0원 이상이어야 합니다.";

    public ProductPriceNotAcceptableException() {
        super(ERROR_MESSAGE_PRODUCT_PRICE_VALUE);
    }

}

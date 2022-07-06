package kitchenpos.exception;

public class ProductPriceEmptyException extends RuntimeException {
    public static final ProductPriceEmptyException PRODUCT_PRICE_EMPTY_EXCEPTION = new ProductPriceEmptyException(
            "상품의 가격은 필수입니다.");

    public ProductPriceEmptyException(String message) {
        super(message);
    }
}

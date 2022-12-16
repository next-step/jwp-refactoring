package kitchenpos.product.exception;

public class ProductPriceException extends RuntimeException {

    public ProductPriceException(ProductPriceExceptionType exceptionType) {
        super(exceptionType.message);
    }

    public ProductPriceException(String message) {
        super(message);
    }
}

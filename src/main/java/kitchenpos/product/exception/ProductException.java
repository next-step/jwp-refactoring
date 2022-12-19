package kitchenpos.product.exception;

public class ProductException extends RuntimeException{
    public ProductException(ProductExceptionType exceptionType) {
        super(exceptionType.message);
    }
}

package kitchenpos.product.exception;

public class NotFoundProductException extends RuntimeException {
    private static final String NOT_FOUND_PRODUCT_MESSAGE = "해당하는 상품을 찾을 수 없습니다. (id = %s)";

    public NotFoundProductException(Long id) {
        super(String.format(NOT_FOUND_PRODUCT_MESSAGE, id));
    }
}

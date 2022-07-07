package kitchenpos.exception;

public class NotFoundProductException extends RuntimeException {
    private static final String message = "존재하지 않는 상품입니다.";

    public static final NotFoundProductException NOT_FOUND_PRODUCT_EXCEPTION = new NotFoundProductException(message);

    public NotFoundProductException() {
        super(message);
    }

    public NotFoundProductException(String message) {
        super(message);
    }
}

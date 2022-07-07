package kitchenpos.exception;

public class NotFoundOrderException extends RuntimeException {
    private static final String message = "존재하지 않는 주문입니다.";

    public static final NotFoundOrderException NOT_FOUND_ORDER_EXCEPTION = new NotFoundOrderException(message);

    public NotFoundOrderException() {
        super(message);
    }

    public NotFoundOrderException(String message) {
        super(message);
    }
}

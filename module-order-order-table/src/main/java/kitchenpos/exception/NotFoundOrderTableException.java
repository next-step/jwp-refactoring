package kitchenpos.exception;

public class NotFoundOrderTableException extends RuntimeException {
    private static final String message = "존재하지 않는 주문테이블입니다.";

    public static final NotFoundOrderTableException NOT_FOUND_ORDER_TABLE_EXCEPTION = new NotFoundOrderTableException(
            message);

    public NotFoundOrderTableException() {
        super(message);
    }

    public NotFoundOrderTableException(String message) {
        super(message);
    }
}

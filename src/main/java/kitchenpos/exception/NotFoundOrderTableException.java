package kitchenpos.exception;

public class NotFoundOrderTableException extends RuntimeException {
    private static final String NOT_FOUND_ORDER_TABLE_MESSAGE = "해당하는 주문테이블을 찾을 수 없습니다. (id = %s)";

    public NotFoundOrderTableException(Long id) {
        super(String.format(NOT_FOUND_ORDER_TABLE_MESSAGE, id));
    }
}

package kitchenpos.common.exception;

public class NotEmptyOrderTableStatusException extends RuntimeException {
    public static final String NOT_EMPTY_ORDER_TABLE_EXCEPTION = "주문 테이블이 비어있어야 합니다";

    public NotEmptyOrderTableStatusException() {
        super(NOT_EMPTY_ORDER_TABLE_EXCEPTION);
    }
}

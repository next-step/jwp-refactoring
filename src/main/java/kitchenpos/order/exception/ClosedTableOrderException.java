package kitchenpos.order.exception;

public class ClosedTableOrderException extends RuntimeException {

    private static final String ERROR_MESSAGE_TABLE_CANNOT_ORDER = "주문종료 상태인 테이블은 주문할 수 없습니다.";

    public ClosedTableOrderException() {
        super(ERROR_MESSAGE_TABLE_CANNOT_ORDER);
    }
}

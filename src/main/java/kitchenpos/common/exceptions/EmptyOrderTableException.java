package kitchenpos.common.exceptions;

public class EmptyOrderTableException extends CustomException {
    public static final String EMPTY_ORDER_TABLE_EXCEPTION = "주문 테이블은 비어 있을 수 없습니다";

    public EmptyOrderTableException() {
        super(EMPTY_ORDER_TABLE_EXCEPTION);
    }
}

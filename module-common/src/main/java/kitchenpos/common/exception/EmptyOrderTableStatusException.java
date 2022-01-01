package kitchenpos.common.exception;

public class EmptyOrderTableStatusException extends RuntimeException {
    public static final String EMPTY_ORDER_TABLE_STATUS_MESSAGE = "빈 주문 테이블 상태는 될 수 없습니다.";

    public EmptyOrderTableStatusException() {
        super(EMPTY_ORDER_TABLE_STATUS_MESSAGE);
    }
}

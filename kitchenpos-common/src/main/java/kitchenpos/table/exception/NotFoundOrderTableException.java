package kitchenpos.table.exception;

public class NotFoundOrderTableException extends RuntimeException {
    public NotFoundOrderTableException() {
        super("존재하지 않는 주문 테이블입니다.");
    }
}

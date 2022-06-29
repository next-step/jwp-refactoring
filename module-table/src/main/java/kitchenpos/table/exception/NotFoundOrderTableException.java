package kitchenpos.table.exception;

public class NotFoundOrderTableException extends RuntimeException {

    public NotFoundOrderTableException() {
        super("테이블을 찾을 수 없습니다.");
    }
}

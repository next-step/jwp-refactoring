package kitchenpos.table.exception;

public class EmptyOrderTableException extends RuntimeException {
    public EmptyOrderTableException() {
        super("해당 테이블은 빈 테이블입니다.");
    }
}

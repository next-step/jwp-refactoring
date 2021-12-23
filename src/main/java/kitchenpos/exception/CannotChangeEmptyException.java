package kitchenpos.exception;

public class CannotChangeEmptyException extends RuntimeException {
    public CannotChangeEmptyException() {
        super("빈 테이블 상태를 변경할 수 없습니다");
    }
}

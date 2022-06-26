package kitchenpos.exception;

public class NotEmptyException extends IllegalArgumentException {
    public NotEmptyException() {
        super("비어 있지 않은 테이블입니다.");
    }

    public NotEmptyException(String s) {
        super(s);
    }
}

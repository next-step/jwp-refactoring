package kitchenpos.exception;

public class EmptyTableException extends IllegalStateException {
    public EmptyTableException() {
        super("비어있는 테이블입니다.");
    }

    public EmptyTableException(String s) {
        super(s);
    }
}

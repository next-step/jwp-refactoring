package kitchenpos.exception;

public class ExistGroupTableException extends IllegalArgumentException {
    public ExistGroupTableException() {
        super("단체로 지정된 테이블입니다.");
    }

    public ExistGroupTableException(String s) {
        super(s);
    }
}

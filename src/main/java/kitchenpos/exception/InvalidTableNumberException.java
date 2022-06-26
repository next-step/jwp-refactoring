package kitchenpos.exception;

public class InvalidTableNumberException extends IllegalArgumentException {
    public InvalidTableNumberException() {
        super("잘못된 테이블 갯수입니다. 2개 이상 묶을 수 있습니다.");
    }

    public InvalidTableNumberException(String s) {
        super(s);
    }
}

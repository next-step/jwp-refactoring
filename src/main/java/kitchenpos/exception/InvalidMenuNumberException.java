package kitchenpos.exception;

public class InvalidMenuNumberException extends IllegalArgumentException {
    public InvalidMenuNumberException() {
        super("올바르지 않은 메뉴 갯수입니다.");
    }

    public InvalidMenuNumberException(String s) {
        super(s);
    }
}

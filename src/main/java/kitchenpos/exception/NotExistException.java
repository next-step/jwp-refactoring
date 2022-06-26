package kitchenpos.exception;

public class NotExistException extends IllegalArgumentException {
    public NotExistException() {
        super("존재하지 않습니다.");
    }

    public NotExistException(String s) {
        super(s);
    }
}

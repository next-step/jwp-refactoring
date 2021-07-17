package kitchenpos.common.exception;

public class CannotUngroupException extends RuntimeException {
    public CannotUngroupException() {
        super("단체지정 해지를 할 수 없습니다.");
    }

    public CannotUngroupException(String message) {
        super(message);
    }
}

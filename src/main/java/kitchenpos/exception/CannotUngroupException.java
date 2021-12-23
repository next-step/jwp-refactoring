package kitchenpos.exception;

public class CannotUngroupException extends RuntimeException {
    public CannotUngroupException() {
        super("그룹 해제가 불가능합니다.");
    }
}

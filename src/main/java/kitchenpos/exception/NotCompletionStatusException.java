package kitchenpos.exception;

public class NotCompletionStatusException extends IllegalStateException {
    public NotCompletionStatusException() {
        super("식사가 완료된 상태가 아닙니다.");
    }

    public NotCompletionStatusException(String s) {
        super(s);
    }
}

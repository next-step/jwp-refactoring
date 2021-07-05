package kitchenpos.table.exception;

public class NoGuestsException extends IllegalArgumentException {
    private static final long serialVersionUID = 540236956800849912L;
    private static final String NO_GUESTS = "0명 이하의 손닙은 업데이트 할 수 없습니다.";

    public NoGuestsException() {
        super(NO_GUESTS);
    }

    public NoGuestsException(String message) {
        super(message);
    }
}

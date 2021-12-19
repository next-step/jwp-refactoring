package kitchenpos.common.exception;

public enum ExceptionMessage {

    REQUIRED("필수값 입니다.");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

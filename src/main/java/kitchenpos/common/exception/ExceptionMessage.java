package kitchenpos.common.exception;

public enum ExceptionMessage {

    WRONG_VALUE("잘못된 값입니다.");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

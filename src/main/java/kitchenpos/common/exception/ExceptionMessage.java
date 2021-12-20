package kitchenpos.common.exception;

public enum ExceptionMessage {

    WRONG_VALUE("잘못된 값입니다."),
    NOT_FOUND_DATA("데이터를 찾을 수 없습니다.");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

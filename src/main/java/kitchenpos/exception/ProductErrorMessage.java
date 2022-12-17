package kitchenpos.exception;

public enum ProductErrorMessage {
    REQUIRED_NAME("이름은 필수 필드입니다."),
    REQUIRED_PRICE("가격은 필수 필드입니다."),
    INVALID_PRICE("가격은 0보다 작을 수 없습니다."),
    NOT_FOUND_BY_ID("ID로 제품을 찾을 수 없습니다.");

    private static final String TITLE = "[ERROR] ";

    private final String message;

    ProductErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return TITLE + message;
    }
}

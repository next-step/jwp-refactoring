package kitchenpos.exception;

public enum MenuErrorMessage {
    REQUIRED_NAME("이름은 필수 필드입니다."),
    REQUIRED_PRICE("가격은 필수 필드입니다."),
    REQUIRED_MENU_GROUP("메뉴 그룹은 필수 필드입니다."),
    INVALID_PRICE("가격은 0보다 작거나 총 금액보다 클 수 없습니다."),
    NOT_FOUND_BY_ID("ID로 메뉴를 찾을 수 없습니다.");

    private static final String TITLE = "[ERROR] ";

    private final String message;

    MenuErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return TITLE + message;
    }
}

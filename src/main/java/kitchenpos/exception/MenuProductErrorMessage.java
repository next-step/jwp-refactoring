package kitchenpos.exception;

public enum MenuProductErrorMessage {
    REQUIRED_MENU("메뉴는 필수 필드입니다."),
    REQUIRED_PRODUCT("제품은 필수 필드입니다."),
    INVALID_QUANTITY("메뉴 제품의 수량은 음수일 수 없습니다.");

    private static final String TITLE = "[ERROR] ";

    private final String message;

    MenuProductErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return TITLE + message;
    }
}

package kitchenpos.menu.exception;

public enum MenuProductExceptionCode {
    REQUIRED_MENU("The menu is a required field."),
    REQUIRED_PRODUCT("The product is a required field."),
    INVALID_QUANTITY("The quantity of menu products cannot be negative.");

    private static final String TITLE = "[ERROR] ";

    private String message;

    MenuProductExceptionCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return TITLE + message;
    }
}

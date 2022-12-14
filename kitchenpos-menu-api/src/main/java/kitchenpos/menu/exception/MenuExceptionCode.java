package kitchenpos.menu.exception;

public enum MenuExceptionCode {
    REQUIRED_NAME("The name is a required field."),
    REQUIRED_PRICE("The price is a required field."),
    REQUIRED_MENU_GROUP("The menu group is a required field."),
    INVALID_PRICE("The price cannot be less than zero or greater than the total amount.");

    private static final String TITLE = "[ERROR] ";

    private String message;

    MenuExceptionCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return TITLE + message;
    }
}

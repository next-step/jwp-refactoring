package kitchenpos.menu.exception;

public enum MenuGroupExceptionCode {
    REQUIRED_NAME("The name is a required field."),
    NOT_FOUND_BY_ID("the menu group not found by id.");

    private static final String TITLE = "[ERROR] ";

    private String message;

    MenuGroupExceptionCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return TITLE + message;
    }
}

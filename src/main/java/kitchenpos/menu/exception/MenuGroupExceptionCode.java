package kitchenpos.menu.exception;

public enum MenuGroupExceptionCode {
    REQUIRED_NAME("The name is a required field.");

    private static final String TITLE = "[ERROR] ";

    private String message;

    MenuGroupExceptionCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return TITLE + message;
    }
}

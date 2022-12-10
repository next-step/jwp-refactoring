package kitchenpos.menu.exception;

public enum ProductExceptionCode {
    REQUIRED_NAME("The name is a required field."),
    REQUIRED_PRICE("The price is a required field."),
    INVALID_PRICE("The price cannot be less than zero."),
    NOT_FOUND_BY_ID("the product not found by id.");

    private static final String TITLE = "[ERROR] ";

    private String message;

    ProductExceptionCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return TITLE + message;
    }
}

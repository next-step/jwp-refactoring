package kitchenpos.order.exception;

public enum OrderLineItemExceptionCode {
    REQUIRED_ORDER("The order is a required field."),
    REQUIRED_MENU("The menu is a required field."),
    INVALID_QUANTITY("The quantity cannot be less than zero");

    private static final String TITLE = "[ERROR] ";

    private String message;

    OrderLineItemExceptionCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return TITLE + message;
    }
}
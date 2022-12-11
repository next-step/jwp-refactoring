package kitchenpos.order.exception;

public enum OrderExceptionCode {
    REQUIRED_ORDER_TABLE("The order table is a required field."),
    CANNOT_BE_CHANGED("The table groups contain tables that are cooking or eating."),
    CANNOT_CHANGE_COMPLETION_ORDER("A completed order cannot be changed."),
    ORDER_TABLE_CANNOT_BE_EMPTY("The order cannot be ordered from an empty order table."),
    MUST_BE_GREATER_THAN_MINIMUM_SIZE("The order menus size cannot be smaller than the minimum size.");

    private static final String TITLE = "[ERROR] ";

    private String message;

    OrderExceptionCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return TITLE + message;
    }
}
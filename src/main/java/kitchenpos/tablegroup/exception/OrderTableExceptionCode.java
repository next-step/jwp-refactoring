package kitchenpos.tablegroup.exception;

public enum OrderTableExceptionCode {
    NON_EMPTY_ORDER_TABLE_CANNOT_BE_INCLUDED_IN_TABLE_GROUP("An non-empty order table cannot be included in a table group."),
    ALREADY_INCLUDED_IN_ANOTHER_TABLE_GROUP("This is an order table that is already included in another table group."),
    INVALID_NUMBER_OF_GUESTS("The number of guests cannot be negative."),
    NUMBER_OF_GUESTS_CANNOT_BE_CHANGED("The number of guests an empty order table cannot be changed.");

    private static final String TITLE = "[ERROR] ";

    private String message;

    OrderTableExceptionCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return TITLE + message;
    }
}
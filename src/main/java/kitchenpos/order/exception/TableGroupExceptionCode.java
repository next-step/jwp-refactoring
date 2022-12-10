package kitchenpos.order.exception;

public enum TableGroupExceptionCode {
    ORDER_TABLES_CANNOT_BE_EMPTY("The order tables cannot be empty collection."),
    MUST_BE_GREATER_THAN_MINIMUM_SIZE("The order tables size cannot be smaller than the minimum size."),
    NOT_FOUND_BY_ID("the table group not found by id.");

    private static final String TITLE = "[ERROR] ";

    private String message;

    TableGroupExceptionCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return TITLE + message;
    }
}

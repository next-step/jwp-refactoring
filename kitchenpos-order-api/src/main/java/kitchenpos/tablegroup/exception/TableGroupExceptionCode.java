package kitchenpos.tablegroup.exception;

public enum TableGroupExceptionCode {
    ORDER_TABLES_CANNOT_BE_EMPTY("The order tables cannot be empty collection."),
    MUST_BE_GREATER_THAN_MINIMUM_SIZE("The order tables size cannot be smaller than the minimum size.");

    private static final String TITLE = "[ERROR] ";

    private String message;

    TableGroupExceptionCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return TITLE + message;
    }
}
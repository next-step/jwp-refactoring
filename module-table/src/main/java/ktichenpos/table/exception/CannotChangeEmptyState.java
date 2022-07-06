package ktichenpos.table.exception;

public class CannotChangeEmptyState extends RuntimeException {
    public static final String INCLUDE_TO_TABLE_GROUP = "table is included tableGroup";
    public static final String NOT_COMPLETED_ORDER = "not completed order exist";

    public CannotChangeEmptyState() {
    }

    public CannotChangeEmptyState(String message) {
        super(message);
    }

    public CannotChangeEmptyState(String message, Throwable cause) {
        super(message, cause);
    }
}

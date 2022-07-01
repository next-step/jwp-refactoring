package kitchenpos.table.exception;

public class CannotChangeEmptyState extends RuntimeException {
    public CannotChangeEmptyState() {
    }

    public CannotChangeEmptyState(String message) {
        super(message);
    }

    public CannotChangeEmptyState(String message, Throwable cause) {
        super(message, cause);
    }
}

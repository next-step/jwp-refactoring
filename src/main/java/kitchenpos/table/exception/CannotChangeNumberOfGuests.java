package kitchenpos.table.exception;

public class CannotChangeNumberOfGuests extends RuntimeException {
    public CannotChangeNumberOfGuests() {
    }

    public CannotChangeNumberOfGuests(String message) {
        super(message);
    }

    public CannotChangeNumberOfGuests(String message, Throwable cause) {
        super(message, cause);
    }
}

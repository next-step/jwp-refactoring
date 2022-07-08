package kitchenpos.table.exception;

public class CannotChangeNumberOfGuests extends RuntimeException {
    public static final String TABLE_EMPTY = "there is a empty table";
    public static final String NEGATIVE_NUMBER_OF_GUESTS = "numberOfGuests can not be negative";

    public CannotChangeNumberOfGuests() {
    }

    public CannotChangeNumberOfGuests(String message) {
        super(message);
    }

    public CannotChangeNumberOfGuests(String message, Throwable cause) {
        super(message, cause);
    }
}

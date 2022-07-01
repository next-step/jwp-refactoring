package kitchenpos.table.exception;

public class CannotMakeTableGroupException extends RuntimeException {
    public static final String TABLE_NOT_EMPTY = "there is not empty table";
    public static final String INCLUDE_ANOTHER_GROUP = "there is table is already include to another table group";


    public CannotMakeTableGroupException() {
    }

    public CannotMakeTableGroupException(String message) {
        super(message);
    }

    public CannotMakeTableGroupException(String message, Throwable cause) {
        super(message, cause);
    }
}

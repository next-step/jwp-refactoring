package kitchenpos.table.exception;

public class CannotMakeTableGroupException extends RuntimeException {
    public static final String NOT_EXIST_TABLE = "there is no saved table";
    public static final String INSUFFICIENT_NUMBER_OF_TABLE = "number of tables in table group should larger than 1";
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

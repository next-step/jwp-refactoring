package ktichenpos.order.exception;

public class CannotMakeOrderException extends RuntimeException {
    public static final String NOT_EXIST_MENU = "not exist menuId";
    public static final String TABLE_IS_EMPTY = "can not make order to empty table";

    public CannotMakeOrderException() {
    }

    public CannotMakeOrderException(String message) {
        super(message);
    }

    public CannotMakeOrderException(String message, Throwable cause) {
        super(message, cause);
    }
}

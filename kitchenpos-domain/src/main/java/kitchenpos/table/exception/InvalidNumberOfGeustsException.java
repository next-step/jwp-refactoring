package kitchenpos.table.exception;

public class InvalidNumberOfGeustsException extends IllegalArgumentException {
    public InvalidNumberOfGeustsException() {
    }

    public InvalidNumberOfGeustsException(String s) {
        super(s);
    }
}

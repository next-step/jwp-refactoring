package kitchenpos.table.exception;

public class NotEmptyOrderTableException extends IllegalArgumentException{
    public NotEmptyOrderTableException() {
        super();
    }

    public NotEmptyOrderTableException(String string) {
        super(string);
    }
}

package kitchenpos.table.exception;

public class NotFoundOrderTableException extends IllegalArgumentException{
    public NotFoundOrderTableException() {
        super();
    }

    public NotFoundOrderTableException(String string) {
        super(string);
    }
}

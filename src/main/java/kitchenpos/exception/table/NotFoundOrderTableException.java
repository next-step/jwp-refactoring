package kitchenpos.exception.table;

public class NotFoundOrderTableException extends IllegalArgumentException{
    public NotFoundOrderTableException() {
        super();
    }

    public NotFoundOrderTableException(String string) {
        super(string);
    }
}

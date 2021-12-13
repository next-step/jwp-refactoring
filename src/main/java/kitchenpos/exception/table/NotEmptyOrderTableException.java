package kitchenpos.exception.table;

public class NotEmptyOrderTableException extends IllegalArgumentException{
    public NotEmptyOrderTableException() {
        super();
    }

    public NotEmptyOrderTableException(String string) {
        super(string);
    }
}

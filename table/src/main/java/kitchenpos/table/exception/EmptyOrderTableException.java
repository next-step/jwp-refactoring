package kitchenpos.table.exception;

public class EmptyOrderTableException extends IllegalArgumentException{
    public EmptyOrderTableException() {
        super();
    }

    public EmptyOrderTableException(String string) {
        super(string);
    }
}

package kitchenpos.exception.table;

public class EmptyOrderTableException extends IllegalArgumentException{
    public EmptyOrderTableException() {
        super();
    }

    public EmptyOrderTableException(String string) {
        super(string);
    }
}

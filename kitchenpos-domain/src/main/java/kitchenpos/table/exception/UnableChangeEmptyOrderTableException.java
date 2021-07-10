package kitchenpos.table.exception;

public class UnableChangeEmptyOrderTableException extends IllegalArgumentException {
    public UnableChangeEmptyOrderTableException() {
    }

    public UnableChangeEmptyOrderTableException(String s) {
        super(s);
    }
}

package kitchenpos.table.exception;

public class UnableOrderCausedByEmptyTableException extends IllegalArgumentException {
    public UnableOrderCausedByEmptyTableException() {
    }

    public UnableOrderCausedByEmptyTableException(String s) {
        super(s);
    }
}

package kitchenpos.table.exception;

public class NotGroupingOrderTableCountException extends IllegalArgumentException{
    public NotGroupingOrderTableCountException() {
        super();
    }

    public NotGroupingOrderTableCountException(String string) {
        super(string);
    }
}

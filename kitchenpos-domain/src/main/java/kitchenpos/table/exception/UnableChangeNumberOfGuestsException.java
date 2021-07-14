package kitchenpos.table.exception;

public class UnableChangeNumberOfGuestsException extends IllegalArgumentException {
    public UnableChangeNumberOfGuestsException() {
    }

    public UnableChangeNumberOfGuestsException(String s) {
        super(s);
    }
}

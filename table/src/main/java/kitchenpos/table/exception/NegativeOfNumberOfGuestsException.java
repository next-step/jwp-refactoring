package kitchenpos.table.exception;

public class NegativeOfNumberOfGuestsException extends IllegalArgumentException{
    public NegativeOfNumberOfGuestsException() {
        super();
    }

    public NegativeOfNumberOfGuestsException(String string) {
        super(string);
    }
}

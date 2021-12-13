package kitchenpos.exception.table;

public class NegativeOfNumberOfGuestsException extends IllegalArgumentException{
    public NegativeOfNumberOfGuestsException() {
        super();
    }

    public NegativeOfNumberOfGuestsException(String string) {
        super(string);
    }
}

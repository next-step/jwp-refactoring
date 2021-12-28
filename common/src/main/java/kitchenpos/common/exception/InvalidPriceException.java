package kitchenpos.common.exception;


public class InvalidPriceException extends IllegalArgumentException{
    public InvalidPriceException() {
        super();
    }

    public InvalidPriceException(String string) {
        super(string);
    }
}

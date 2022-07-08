package kitchenpos.menu.exception;

public class InvalidValueException extends IllegalArgumentException {

    public InvalidValueException() {
    }

    public InvalidValueException(String s) {
        super(s);
    }
}

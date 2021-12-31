package kitchenpos.domain.menu.exception;

public class IllegalMenuNameException extends IllegalArgumentException {
    public IllegalMenuNameException(String message) {
        super(message);
    }
}

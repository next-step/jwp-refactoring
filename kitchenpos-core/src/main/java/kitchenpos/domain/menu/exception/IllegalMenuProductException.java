package kitchenpos.domain.menu.exception;

public class IllegalMenuProductException extends IllegalArgumentException {
    public IllegalMenuProductException(String message) {
        super(message);
    }
}

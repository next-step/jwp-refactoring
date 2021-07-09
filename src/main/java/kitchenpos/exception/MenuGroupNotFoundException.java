package kitchenpos.exception;

public class MenuGroupNotFoundException extends RuntimeException {
    public MenuGroupNotFoundException(String message) {
        super(message);
    }
}

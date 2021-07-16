package kitchenpos.menu.application;

public class MenuProductNotFoundException extends RuntimeException {
    public MenuProductNotFoundException() {
    }

    public MenuProductNotFoundException(String message) {
        super(message);
    }
}

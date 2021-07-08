package kitchenpos.tobe.menu.application;

public class MenuNotFoundException extends RuntimeException {
    public MenuNotFoundException() {
    }

    public MenuNotFoundException(String message) {
        super(message);
    }
}

package kitchenpos.menu.application;

public class MenuNotMatchException extends RuntimeException {

    public MenuNotMatchException() {
    }

    public MenuNotMatchException(String message) {
        super(message);
    }
}

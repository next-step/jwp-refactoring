package kitchenpos.menugroup.application;

public class MenuGroupNotFoundException extends RuntimeException {

    public MenuGroupNotFoundException() {
    }

    public MenuGroupNotFoundException(String message) {
        super(message);
    }
}

package kitchenpos.menu.exception;

public class NotFoundMenuException extends IllegalArgumentException{
    public NotFoundMenuException() {
        super();
    }

    public NotFoundMenuException(String string) {
        super(string);
    }
}

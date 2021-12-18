package kitchenpos.exception.menu;

public class NotFoundMenuException extends IllegalArgumentException{
    public NotFoundMenuException() {
        super();
    }

    public NotFoundMenuException(String string) {
        super(string);
    }
}

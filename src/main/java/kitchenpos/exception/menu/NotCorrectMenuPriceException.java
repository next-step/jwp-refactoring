package kitchenpos.exception.menu;

public class NotCorrectMenuPriceException extends IllegalArgumentException{
    public NotCorrectMenuPriceException() {
        super();
    }

    public NotCorrectMenuPriceException(String string) {
        super(string);
    }
}

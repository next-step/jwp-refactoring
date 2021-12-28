package kitchenpos.menu.exception;

public class NotRegistedMenuOrderException extends IllegalArgumentException{
    public NotRegistedMenuOrderException() {
        super();
    }

    public NotRegistedMenuOrderException(String string) {
        super(string);
    }
}

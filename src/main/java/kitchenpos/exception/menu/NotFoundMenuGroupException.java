package kitchenpos.exception.menu;

public class NotFoundMenuGroupException extends IllegalArgumentException{
    public NotFoundMenuGroupException() {
        super();
    }

    public NotFoundMenuGroupException(String string) {
        super(string);
    }
}

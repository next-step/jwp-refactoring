package kitchenpos.menugroup.exception;

public class NotFoundMenuGroupException extends IllegalArgumentException{
    public NotFoundMenuGroupException() {
        super();
    }

    public NotFoundMenuGroupException(String string) {
        super(string);
    }
}

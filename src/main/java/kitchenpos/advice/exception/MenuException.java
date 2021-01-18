package kitchenpos.advice.exception;

public class MenuException extends RuntimeException {

    public MenuException(String message, long id) {
        super(String.format(message + "id : %d", id));
    }
}

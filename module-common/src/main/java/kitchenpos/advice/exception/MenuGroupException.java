package kitchenpos.advice.exception;

public class MenuGroupException extends BusinessException {

    public MenuGroupException(String message, long id) {
        super(String.format(message + "id : %d", id));
    }
}

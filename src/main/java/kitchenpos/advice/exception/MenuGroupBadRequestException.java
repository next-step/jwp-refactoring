package kitchenpos.advice.exception;

public class MenuGroupBadRequestException extends RuntimeException {

    public MenuGroupBadRequestException(String message, long id) {
        super(String.format(message + "id : %d", id));
    }
}

package kitchenpos.advice.exception;

public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, long id) {
        super(String.format(message + "id : %d", id));
    }
}

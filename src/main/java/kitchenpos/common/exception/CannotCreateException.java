package kitchenpos.common.exception;

import org.springframework.http.HttpStatus;

public class CannotCreateException extends KitchenPosException {

    public CannotCreateException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

    public CannotCreateException(ExceptionType exceptionType) {
        super(HttpStatus.BAD_REQUEST, exceptionType);
    }
}

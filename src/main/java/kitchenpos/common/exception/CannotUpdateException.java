package kitchenpos.common.exception;

import org.springframework.http.HttpStatus;

public class CannotUpdateException extends KitchenPosException {

    public CannotUpdateException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

    public CannotUpdateException(ExceptionType exceptionType) {
        super(HttpStatus.BAD_REQUEST, exceptionType);
    }

    public CannotUpdateException(HttpStatus status, ExceptionType exceptionType) {
        super(status, exceptionType);
    }
}

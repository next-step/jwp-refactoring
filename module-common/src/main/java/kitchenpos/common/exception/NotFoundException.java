package kitchenpos.common.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends KitchenPosException {

    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

    public NotFoundException(ExceptionType exceptionType) {
        super(HttpStatus.NOT_FOUND, exceptionType);
    }
}

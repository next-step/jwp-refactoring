package kitchenpos.common.exception;

import org.springframework.http.HttpStatus;

public class KitchenPosException extends RuntimeException {

    private final HttpStatus status;
    private final String message;

    public KitchenPosException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public KitchenPosException(HttpStatus status, ExceptionType exceptionType) {
        this.status = status;
        this.message = exceptionType.getMessage();
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

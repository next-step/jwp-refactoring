package kitchenpos.common.exceptions;

import org.springframework.http.HttpStatus;

public abstract class CustomException extends RuntimeException {
    private final HttpStatus status;

    protected CustomException(HttpStatus status) {
        super();
        this.status = status;
    }

    protected CustomException(final HttpStatus status, final String message) {
        super(message);
        this.status = status;
    }

    protected CustomException(final String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getHttpStatus() {
        return status;
    }
}

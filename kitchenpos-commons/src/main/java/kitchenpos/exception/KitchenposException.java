package kitchenpos.exception;

import org.springframework.http.HttpStatus;

public class KitchenposException extends RuntimeException {

    private final KitchenposExceptionMessage exceptionMessage;

    public KitchenposException(final KitchenposExceptionMessage message) {
        super(message.getMessage());
        this.exceptionMessage = message;
    }

    public HttpStatus getStatus() {
        return this.exceptionMessage.getStatus();
    }
}

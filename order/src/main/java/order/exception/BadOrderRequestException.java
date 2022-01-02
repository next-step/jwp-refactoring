package order.exception;

import common.config.exception.NotValidRequestException;

public class BadOrderRequestException extends NotValidRequestException {
    public BadOrderRequestException(String message) {
        super(message);
    }
}

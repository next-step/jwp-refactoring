package common.error.exception;

import java.util.NoSuchElementException;

public class NotExistException extends NoSuchElementException {
    public NotExistException(String message) {
        super(message);
    }
}

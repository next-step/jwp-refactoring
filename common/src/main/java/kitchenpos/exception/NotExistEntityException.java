package kitchenpos.exception;

public class NotExistEntityException extends RuntimeException {

    public NotExistEntityException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}

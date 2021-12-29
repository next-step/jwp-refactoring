package kitchenpos.common.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException() {
    }

    public NotFoundException(ExceptionMessage exceptionMessage) {
        super(exceptionMessage.getMessage());
    }
}

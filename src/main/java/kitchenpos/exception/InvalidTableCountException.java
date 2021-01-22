package kitchenpos.exception;

public class InvalidTableCountException extends BadRequestException {
    public InvalidTableCountException(String message) {
        super(message);
    }
}

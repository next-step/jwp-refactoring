package kitchenpos.exception;

public class AlreadyCompleteException extends BadRequestException {
    public AlreadyCompleteException(String message) {
        super(message);
    }
}

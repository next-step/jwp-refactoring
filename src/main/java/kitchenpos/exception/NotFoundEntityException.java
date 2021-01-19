package kitchenpos.exception;

public class NotFoundEntityException extends BadRequestException {
    public NotFoundEntityException(String message) {
        super(message);
    }
}

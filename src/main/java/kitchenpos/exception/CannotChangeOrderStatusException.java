package kitchenpos.exception;

public class CannotChangeOrderStatusException extends BadRequestException {
    public CannotChangeOrderStatusException() {
        super(ErrorMessage.CANNOT_CHANGE_STATUS);
    }
}

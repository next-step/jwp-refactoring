package kitchenpos.exception;

public class CannotChangeByOrderStatusException extends BadRequestException {

    public CannotChangeByOrderStatusException() {
        super(ErrorMessage.CANNOT_CHANGE_BY_ORDER_STATUS);
    }
}

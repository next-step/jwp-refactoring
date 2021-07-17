package kitchenpos.order.exception;

public class CannotChangeNumberOfGuestException extends RuntimeException {

    public CannotChangeNumberOfGuestException(String message) {
        super(message);
    }
}

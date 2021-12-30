package kitchenpos.order.exception;

import kitchenpos.exception.InvalidException;

public class InvalidOrderState extends InvalidException {
    public InvalidOrderState() {
        super();
    }

    public InvalidOrderState(String message) {
        super(message);
    }
}

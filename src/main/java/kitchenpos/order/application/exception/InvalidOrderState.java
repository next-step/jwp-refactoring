package kitchenpos.order.application.exception;

import kitchenpos.common.InvalidException;

public class InvalidOrderState extends InvalidException {
    public InvalidOrderState() {
        super();
    }

    public InvalidOrderState(String message) {
        super(message);
    }
}

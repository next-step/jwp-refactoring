package kitchenpos.order.exception;

import kitchenpos.exception.InvalidException;

public class InvalidTableState extends InvalidException {
    public InvalidTableState() {
        super();
    }

    public InvalidTableState(String message) {
        super(message);
    }
}

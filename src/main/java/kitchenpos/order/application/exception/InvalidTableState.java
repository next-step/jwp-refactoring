package kitchenpos.order.application.exception;

import kitchenpos.common.InvalidException;

public class InvalidTableState extends InvalidException {
    public InvalidTableState() {
        super();
    }

    public InvalidTableState(String message) {
        super(message);
    }
}

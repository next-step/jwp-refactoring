package kitchenpos.menu.application.exception;

import kitchenpos.common.InvalidException;

public class InvalidPrice extends InvalidException {
    public InvalidPrice() {
        super();
    }

    public InvalidPrice(String message) {
        super(message);
    }
}

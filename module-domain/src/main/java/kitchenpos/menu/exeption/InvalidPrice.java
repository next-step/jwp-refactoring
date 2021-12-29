package kitchenpos.menu.exeption;

import kitchenpos.exception.InvalidException;

public class InvalidPrice extends InvalidException {
    public InvalidPrice() {
        super();
    }

    public InvalidPrice(String message) {
        super(message);
    }
}

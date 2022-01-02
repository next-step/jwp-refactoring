package kitchenpos.order.exception;

import javax.persistence.EntityNotFoundException;

public class NotCreateOrderException extends EntityNotFoundException {
    public NotCreateOrderException(String message) {
        super(message);
    }
}

package kitchenpos.order.exception;

import javax.persistence.EntityNotFoundException;

public class NotFoundOrderException extends EntityNotFoundException {
    public NotFoundOrderException() {
    }
}

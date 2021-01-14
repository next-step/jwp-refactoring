package kitchenpos.domain.order.exceptions;

import javax.persistence.EntityNotFoundException;

public class OrderEntityNotFoundException extends EntityNotFoundException {
    public OrderEntityNotFoundException(final String message) {
        super(message);
    }
}

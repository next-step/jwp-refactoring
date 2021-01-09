package kitchenpos.domain.order.exceptions;

import kitchenpos.exceptions.EntityNotFoundException;

public class OrderEntityNotFoundException extends EntityNotFoundException {
    public OrderEntityNotFoundException(final String message) {
        super(message);
    }
}

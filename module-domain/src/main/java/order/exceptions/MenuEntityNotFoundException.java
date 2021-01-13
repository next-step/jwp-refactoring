package kitchenpos.domain.order.exceptions;

import javax.persistence.EntityNotFoundException;

public class MenuEntityNotFoundException extends EntityNotFoundException {
    public MenuEntityNotFoundException(final String message) {
        super(message);
    }
}

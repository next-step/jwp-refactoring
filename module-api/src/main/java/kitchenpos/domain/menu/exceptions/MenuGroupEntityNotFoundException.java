package kitchenpos.domain.menu.exceptions;

import kitchenpos.exceptions.EntityNotFoundException;

public class MenuGroupEntityNotFoundException extends EntityNotFoundException {
    public MenuGroupEntityNotFoundException(final String message) {
        super(message);
    }
}

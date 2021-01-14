package kitchenpos.domain.menu.exceptions;

import javax.persistence.EntityNotFoundException;

public class MenuGroupEntityNotFoundException extends EntityNotFoundException {
    public MenuGroupEntityNotFoundException(final String message) {
        super(message);
    }
}

package kitchenpos.domain.exceptions.menu;

import kitchenpos.exceptions.EntityNotFoundException;

public class MenuGroupEntityNotFoundException extends EntityNotFoundException {
    public MenuGroupEntityNotFoundException(final String message) {
        super(message);
    }
}

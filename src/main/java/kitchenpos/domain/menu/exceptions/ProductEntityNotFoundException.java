package kitchenpos.domain.menu.exceptions;

import kitchenpos.exceptions.EntityNotFoundException;

public class ProductEntityNotFoundException extends EntityNotFoundException {
    public ProductEntityNotFoundException(final String message) {
        super(message);
    }
}

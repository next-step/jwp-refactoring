package kitchenpos.domain.menu.exceptions;

import javax.persistence.EntityNotFoundException;

public class ProductEntityNotFoundException extends EntityNotFoundException {
    public ProductEntityNotFoundException(final String message) {
        super(message);
    }
}

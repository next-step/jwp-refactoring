package kitchenpos.domain.tablegroup.exceptions;

import kitchenpos.exceptions.EntityNotFoundException;

public class TableGroupEntityNotFoundException extends EntityNotFoundException {
    public TableGroupEntityNotFoundException(final String message) {
        super(message);
    }
}

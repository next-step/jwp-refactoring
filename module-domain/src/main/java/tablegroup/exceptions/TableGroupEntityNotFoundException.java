package kitchenpos.domain.tablegroup.exceptions;

import javax.persistence.EntityNotFoundException;

public class TableGroupEntityNotFoundException extends EntityNotFoundException {
    public TableGroupEntityNotFoundException(final String message) {
        super(message);
    }
}

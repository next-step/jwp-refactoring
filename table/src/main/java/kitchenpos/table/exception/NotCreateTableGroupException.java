package kitchenpos.table.exception;

import javax.persistence.EntityNotFoundException;

public class NotCreateTableGroupException extends EntityNotFoundException {
    public NotCreateTableGroupException(String message) {
        super(message);
    }
}
